package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 单日路线规划
 */
@Data
public class SpotRoutePlanDayVO implements Serializable {

    /**
     * 第几天
     */
    private Integer dayNumber;

    /**
     * 当天景点
     */
    private List<SpotVO> spotList;

    /**
     * 景点名称列表
     */
    private List<String> spotNameList;

    /**
     * 当天总里程（公里）
     */
    private Double totalDistance;

    /**
     * 当天预估花费
     */
    private BigDecimal estimatedCost;

    /**
     * 当天路线说明
     */
    private String routeDescription;

    private static final long serialVersionUID = 1L;
}
