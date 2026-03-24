package com.xiaobaitiao.springbootinit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 旅游AI配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "tourism.ai")
public class TourismAiProperties {

    /**
     * 是否启用AI功能
     */
    private boolean enabled = true;

    /**
     * AI服务提供商
     */
    private String provider = "doubao";

    /**
     * API基础地址
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 温度参数（创造性）
     */
    private double temperature = 0.7;

    /**
     * 最大token数
     */
    private int maxTokens = 1500;

    /**
     * 超时时间（秒）
     */
    private int timeoutSeconds = 30;

    /**
     * 最大并发数
     */
    private int maxConcurrent = 5;

    /**
     * 每分钟最大请求数（限流）
     */
    private int maxRequestsPerMinute = 30;

    /**
     * 最大重试次数
     */
    private int maxRetries = 2;

    /**
     * 熔断失败阈值（连续失败次数）
     */
    private int circuitBreakerThreshold = 5;

    /**
     * 熔断恢复时间（秒）
     */
    private int circuitBreakerRecoverySeconds = 60;
}
