package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 多智能协作旅游规划结果
 */
@Data
public class TourismMultiAgentVO implements Serializable {

    /**
     * 偏好分析
     */
    private TourismPreferenceVO preferenceAnalysis;

    /**
     * 推荐智能体结果
     */
    private List<SpotVO> recommendationSpotList;

    /**
     * 规划智能体结果
     */
    private SpotRoutePlanVO routePlan;

    /**
     * 讲解智能体结果
     */
    private String explanationMarkdown;

    /**
     * 推荐摘要
     */
    private String recommendationSummary;

    /**
     * 路线摘要
     */
    private String routeSummary;

    /**
     * 推荐亮点
     */
    private List<String> recommendationHighlightList;

    /**
     * 路线亮点
     */
    private List<String> routeHighlightList;

    /**
     * 协作流程摘要
     */
    private List<String> workflowSummary;

    private static final long serialVersionUID = 1L;
}
