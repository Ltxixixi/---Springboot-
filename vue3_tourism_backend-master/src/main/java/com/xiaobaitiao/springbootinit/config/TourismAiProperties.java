package com.xiaobaitiao.springbootinit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "tourism.ai")
public class TourismAiProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 提供方
     */
    private String provider = "doubao";

    /**
     * OpenAI 兼容接口地址
     */
    private String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";

    /**
     * API Key
     */
    private String apiKey;

    /**
     * 模型名
     */
    private String model = "doubao-seed-1-6-251015";

    /**
     * 随机度
     */
    private Double temperature = 0.6D;

    /**
     * 最大 token 数
     */
    private Integer maxTokens = 1500;
}
