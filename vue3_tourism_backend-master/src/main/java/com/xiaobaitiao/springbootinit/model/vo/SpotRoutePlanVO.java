package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 智能路线规划结果
 */
@Data
public class SpotRoutePlanVO implements Serializable {

    /**
     * 路线标题
     */
    private String routeTitle;

    /**
     * 路线摘要
     */
    private String routeDescription;

    /**
     * 天数
     */
    private Integer totalDays;

    /**
     * 景点数量
     */
    private Integer totalSpotCount;

    /**
     * 预估总花费
     */
    private BigDecimal totalEstimatedCost;

    /**
     * 封面图
     */
    private String coverSpotAvatar;

    /**
     * 景点 id 列表
     */
    private List<String> spotIdList;

    /**
     * 景点名称列表
     */
    private List<String> spotNameList;

    /**
     * 路线距离列表
     */
    private List<Double> spotDistanceList;

    /**
     * 匹配到的标签
     */
    private List<String> matchedTagList;

    /**
     * 全部景点
     */
    private List<SpotVO> spotList;

    /**
     * 每日规划
     */
    private List<SpotRoutePlanDayVO> dayPlanList;

    private static final long serialVersionUID = 1L;
}
