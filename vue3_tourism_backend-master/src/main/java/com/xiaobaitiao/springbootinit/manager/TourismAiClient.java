package com.xiaobaitiao.springbootinit.manager;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 OpenAI 兼容协议的 AI 客户端
 */
@Slf4j
@Component
public class TourismAiClient {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Resource
    private TourismAiProperties tourismAiProperties;

    public boolean isAvailable() {
        return tourismAiProperties.isEnabled()
                && StringUtils.isNotBlank(getApiKey())
                && StringUtils.isNotBlank(getBaseUrl())
                && StringUtils.isNotBlank(getModel());
    }

    public String chat(String userPrompt) {
        List<Map<String, String>> messageList = new ArrayList<>();
        messageList.add(buildMessage("user", userPrompt));
        return chat(messageList);
    }

    public String chat(String systemPrompt, String userPrompt) {
        List<Map<String, String>> messageList = new ArrayList<>();
        if (StringUtils.isNotBlank(systemPrompt)) {
            messageList.add(buildMessage("system", systemPrompt));
        }
        messageList.add(buildMessage("user", userPrompt));
        return chat(messageList);
    }

    public String chat(List<Map<String, String>> messageList) {
        if (!isAvailable()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "AI 服务未配置完成，请检查 TOURISM_AI_API_KEY、TOURISM_AI_BASE_URL、TOURISM_AI_MODEL");
        }
        Map<String, Object> requestBodyMap = new LinkedHashMap<>();
        requestBodyMap.put("model", getModel());
        requestBodyMap.put("messages", messageList);
        requestBodyMap.put("temperature", tourismAiProperties.getTemperature());
        requestBodyMap.put("max_tokens", tourismAiProperties.getMaxTokens());

        String requestBodyJson = JSONUtil.toJsonStr(requestBodyMap);
        Request request = new Request.Builder()
                .url(buildChatUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + getApiKey())
                .post(RequestBody.create(requestBodyJson, JSON_MEDIA_TYPE))
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String responseBody = response.body() == null ? "" : response.body().string();
            if (!response.isSuccessful()) {
                log.error("AI 服务调用失败，url={}, model={}, status={}, body={}",
                        buildChatUrl(), getModel(), response.code(), responseBody);
                log.error("AI Key 摘要: {}", maskKey(getApiKey()));
                throw new BusinessException(ErrorCode.OPERATION_ERROR,
                        "AI 服务调用失败，状态码：" + response.code() + "，响应：" + responseBody);
            }
            return parseContent(responseBody);
        } catch (IOException e) {
            log.error("AI 服务调用异常", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 服务调用异常：" + e.getMessage());
        }
    }

    private String buildChatUrl() {
        return StringUtils.removeEnd(getBaseUrl(), "/") + "/chat/completions";
    }

    private String parseContent(String responseBody) {
        JSONObject jsonObject = JSONUtil.parseObj(responseBody);
        JSONArray choiceList = jsonObject.getJSONArray("choices");
        if (choiceList == null || choiceList.isEmpty()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 服务未返回有效内容：" + responseBody);
        }
        JSONObject firstChoice = choiceList.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        String content = message == null ? null : message.getStr("content");
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 服务返回内容为空：" + responseBody);
        }
        return content.trim();
    }

    private Map<String, String> buildMessage(String role, String content) {
        Map<String, String> message = new LinkedHashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private String getApiKey() {
        return normalizeValue(tourismAiProperties.getApiKey());
    }

    private String getBaseUrl() {
        return normalizeValue(tourismAiProperties.getBaseUrl());
    }

    private String getModel() {
        return normalizeValue(tourismAiProperties.getModel());
    }

    private String normalizeValue(String value) {
        String trimmedValue = StringUtils.trimToEmpty(value);
        if (trimmedValue.length() >= 2) {
            boolean wrappedByDoubleQuotes = trimmedValue.startsWith("\"") && trimmedValue.endsWith("\"");
            boolean wrappedBySingleQuotes = trimmedValue.startsWith("'") && trimmedValue.endsWith("'");
            if (wrappedByDoubleQuotes || wrappedBySingleQuotes) {
                return StringUtils.trimToEmpty(trimmedValue.substring(1, trimmedValue.length() - 1));
            }
        }
        return trimmedValue;
    }

    private String maskKey(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            return "empty";
        }
        if (apiKey.length() <= 8) {
            return "***";
        }
        return apiKey.substring(0, 4) + "..." + apiKey.substring(apiKey.length() - 4) + " (len=" + apiKey.length() + ")";
    }
}
