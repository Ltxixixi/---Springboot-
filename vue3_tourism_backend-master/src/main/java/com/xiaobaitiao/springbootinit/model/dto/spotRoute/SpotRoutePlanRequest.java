package com.xiaobaitiao.springbootinit.model.dto.spotRoute;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 智能路线规划请求
 */
@Data
public class SpotRoutePlanRequest implements Serializable {

    /**
     * 游玩天数
     */
    private Integer dayCount;

    /**
     * 预算
     */
    private BigDecimal budget;

    /**
     * 目标地区关键字
     */
    private String spotLocation;

    /**
     * 偏好标签
     */
    private List<String> spotTagList;

    private static final long serialVersionUID = 1L;
}
