package com.xiaobaitiao.springbootinit.manager;

import cn.hutool.json.JSONUtil;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.config.TourismAiProperties;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 带稳定性保障的AI客户端
 *
 * 保障机制：
 * 1. 超时控制：防止API调用无限等待
 * 2. 限流控制：限制每分钟请求数
 * 3. 并发控制：限制最大并发数
 * 4. 熔断器：连续失败后自动熔断，恢复后自动开启
 * 5. 降级策略：服务不可用时返回友好的降级响应
 * 6. 重试机制：失败后自动重试
 *
 * 论文支撑："引入超时控制、并发限流与熔断降级策略，保障系统稳定性与成本可控"
 */
@Slf4j
@Component
public class StableTourismAiClient {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Resource
    private TourismAiProperties aiProperties;

    // ==================== 限流器 ====================
    private volatile AtomicInteger requestCount = new AtomicInteger(0);
    private volatile AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

    // ==================== 并发控制 ====================
    private volatile Semaphore semaphore;

    // ==================== 熔断器 ====================
    private volatile AtomicInteger failureCount = new AtomicInteger(0);
    private volatile AtomicLong lastFailureTime = new AtomicLong(0);
    private volatile boolean circuitOpen = false;

    @PostConstruct
    public void init() {
        this.semaphore = new Semaphore(aiProperties.getMaxConcurrent());
        log.info("AI稳定性保障初始化完成，最大并发: {}, 限流阈值: {}/min",
                aiProperties.getMaxConcurrent(), aiProperties.getMaxRequestsPerMinute());
    }

    /**
     * 带完整稳定性保障的AI调用
     *
     * @param systemPrompt 系统提示词
     * @param userPrompt 用户输入
     * @return AI响应内容
     */
    public String chat(String systemPrompt, String userPrompt) {
        // 1. 检查熔断状态
        if (isCircuitOpen()) {
            log.warn("AI服务熔断中，降级返回");
            return getDegradedResponse();
        }

        // 2. 限流检查
        if (!tryAcquire()) {
            log.warn("AI服务请求超过限流阈值，降级返回");
            return getDegradedResponse();
        }

        // 3. 并发控制
        try {
            if (!semaphore.tryAcquire(aiProperties.getTimeoutSeconds(), TimeUnit.SECONDS)) {
                log.warn("AI服务并发满载，降级返回");
                return getDegradedResponse();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return getDegradedResponse();
        }

        try {
            // 4. 带重试的调用
            String result = callWithRetry(systemPrompt, userPrompt);

            // 5. 成功，重置失败计数
            onSuccess();

            return result;
        } catch (Exception e) {
            // 6. 失败，记录并可能触发熔断
            onFailure(e);
            throw e;
        } finally {
            semaphore.release();
        }
    }

    /**
     * 带重试的API调用
     */
    private String callWithRetry(String systemPrompt, String userPrompt) {
        Exception lastException = null;

        for (int i = 0; i <= aiProperties.getMaxRetries(); i++) {
            try {
                return callApi(systemPrompt, userPrompt);
            } catch (Exception e) {
                lastException = e;
                log.warn("AI API调用失败 (尝试 {}/{}): {}",
                        i + 1, aiProperties.getMaxRetries() + 1, e.getMessage());

                if (i < aiProperties.getMaxRetries()) {
                    try {
                        // 指数退避
                        Thread.sleep((long) Math.pow(2, i) * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        throw new BusinessException(ErrorCode.OPERATION_ERROR,
                "AI服务调用失败: " + (lastException != null ? lastException.getMessage() : "未知错误"));
    }

    /**
     * 实际调用API
     */
    private String callApi(String systemPrompt, String userPrompt) {
        if (!aiProperties.isEnabled()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI服务未启用");
        }

        String apiKey = normalizeValue(aiProperties.getApiKey());
        String baseUrl = normalizeValue(aiProperties.getBaseUrl());
        String model = normalizeValue(aiProperties.getModel());

        if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(baseUrl) || StringUtils.isBlank(model)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI服务未配置完成");
        }

        // 构建消息列表
        java.util.List<Map<String, String>> messageList = new ArrayList<>();
        if (StringUtils.isNotBlank(systemPrompt)) {
            messageList.add(buildMessage("system", systemPrompt));
        }
        messageList.add(buildMessage("user", userPrompt));

        // 构建请求
        Map<String, Object> requestBodyMap = new LinkedHashMap<>();
        requestBodyMap.put("model", model);
        requestBodyMap.put("messages", messageList);
        requestBodyMap.put("temperature", aiProperties.getTemperature());
        requestBodyMap.put("max_tokens", aiProperties.getMaxTokens());

        String requestBodyJson = JSONUtil.toJsonStr(requestBodyMap);

        String chatUrl = StringUtils.removeEnd(baseUrl, "/") + "/chat/completions";

        Request request = new Request.Builder()
                .url(chatUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBodyJson, JSON_MEDIA_TYPE))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseBody = response.body() == null ? "" : response.body().string();

            if (!response.isSuccessful()) {
                log.error("AI服务调用失败，url={}, model={}, status={}, body={}",
                        chatUrl, model, response.code(), responseBody);
                throw new BusinessException(ErrorCode.OPERATION_ERROR,
                        "AI服务调用失败，状态码：" + response.code());
            }

            return parseContent(responseBody);
        } catch (BusinessException e) {
            throw e;
        } catch (IOException e) {
            log.error("AI服务调用IO异常", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI服务调用异常：" + e.getMessage());
        }
    }

    /**
     * 解析响应内容
     */
    private String parseContent(String responseBody) {
        try {
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(responseBody);
            cn.hutool.json.JSONArray choiceList = jsonObject.getJSONArray("choices");

            if (choiceList == null || choiceList.isEmpty()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI服务未返回有效内容");
            }

            cn.hutool.json.JSONObject firstChoice = choiceList.getJSONObject(0);
            cn.hutool.json.JSONObject message = firstChoice.getJSONObject("message");

            String content = message == null ? null : message.getStr("content");
            if (StringUtils.isBlank(content)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI服务返回内容为空");
            }

            return content.trim();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("解析AI响应失败: {}", responseBody, e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "解析AI响应失败");
        }
    }

    /**
     * 构建消息
     */
    private Map<String, String> buildMessage(String role, String content) {
        Map<String, String> message = new LinkedHashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    /**
     * 限流检查
     */
    private boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long window = windowStart.get();

        // 每分钟重置计数器
        if (now - window >= 60000) {
            synchronized (this) {
                if (now - windowStart.get() >= 60000) {
                    requestCount.set(0);
                    windowStart.set(now);
                }
            }
        }

        return requestCount.incrementAndGet() <= aiProperties.getMaxRequestsPerMinute();
    }

    /**
     * 检查熔断状态
     */
    private boolean isCircuitOpen() {
        if (!circuitOpen) {
            return false;
        }

        // 检查是否到达恢复时间
        long now = System.currentTimeMillis();
        if (now - lastFailureTime.get() >= aiProperties.getCircuitBreakerRecoverySeconds() * 1000L) {
            log.info("AI服务熔断恢复，尝试重新开放");
            circuitOpen = false;
            failureCount.set(0);
            return false;
        }

        return true;
    }

    /**
     * 成功回调
     */
    private void onSuccess() {
        failureCount.set(0);
    }

    /**
     * 失败回调
     */
    private void onFailure(Exception e) {
        int failures = failureCount.incrementAndGet();
        lastFailureTime.set(System.currentTimeMillis());

        if (failures >= aiProperties.getCircuitBreakerThreshold()) {
            if (!circuitOpen) {
                log.error("AI服务连续失败{}次，触发熔断", failures);
                circuitOpen = true;
            }
        }
    }

    /**
     * 降级响应
     */
    private String getDegradedResponse() {
        return "抱歉，AI服务暂时繁忙，请稍后重试。你也可以尝试简化问题或稍后再试。";
    }

    /**
     * 规范化配置值
     */
    private String normalizeValue(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.length() >= 2) {
            boolean wrappedByDoubleQuotes = trimmed.startsWith("\"") && trimmed.endsWith("\"");
            boolean wrappedBySingleQuotes = trimmed.startsWith("'") && trimmed.endsWith("'");
            if (wrappedByDoubleQuotes || wrappedBySingleQuotes) {
                return trimmed.substring(1, trimmed.length() - 1).trim();
            }
        }
        return trimmed;
    }

    /**
     * 检查AI服务是否可用
     */
    public boolean isAvailable() {
        return aiProperties.isEnabled()
                && StringUtils.isNotBlank(aiProperties.getApiKey())
                && StringUtils.isNotBlank(aiProperties.getBaseUrl())
                && StringUtils.isNotBlank(aiProperties.getModel())
                && !isCircuitOpen();
    }

    /**
     * 获取服务状态
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("enabled", aiProperties.isEnabled());
        status.put("circuitOpen", circuitOpen);
        status.put("failureCount", failureCount.get());
        status.put("available", isAvailable());
        status.put("currentConcurrency", aiProperties.getMaxConcurrent() - semaphore.availablePermits());
        status.put("maxConcurrency", aiProperties.getMaxConcurrent());
        return status;
    }

    /**
     * 手动重置熔断器
     */
    public void resetCircuitBreaker() {
        circuitOpen = false;
        failureCount.set(0);
        log.info("AI服务熔断器已手动重置");
    }
}
