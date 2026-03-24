package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * AI 改线解析结果
 */
@Data
public class RouteAdjustmentAnalysisVO implements Serializable {

    /**
     * 原始改线描述
     */
    private String originalInstruction;

    /**
     * AI/规则理解摘要
     */
    private String interpretedSummary;

    /**
     * 识别到的动作
     */
    private List<String> actionList;

    /**
     * 追加标签
     */
    private List<String> includeTagList;

    /**
     * 排除标签
     */
    private List<String> excludeTagList;

    /**
     * 识别到的节奏
     */
    private String suggestedPaceType;

    private static final long serialVersionUID = 1L;
}
