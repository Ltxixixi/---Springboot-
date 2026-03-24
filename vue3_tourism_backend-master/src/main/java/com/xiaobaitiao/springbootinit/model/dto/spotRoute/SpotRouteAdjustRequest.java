package com.xiaobaitiao.springbootinit.model.dto.spotRoute;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 路线微调请求
 */
@Data
public class SpotRouteAdjustRequest implements Serializable {

    /**
     * 原始规划请求
     */
    private SpotRoutePlanRequest originalRequest;

    /**
     * 微调类型：RELAXED / SAVE_BUDGET / ADD_NIGHT_VIEW
     */
    private String adjustType;

    /**
     * 当前规划总花费，用于“更省钱”时推导新预算
     */
    private BigDecimal currentEstimatedCost;

    /**
     * 自然语言微调指令
     */
    private String customInstruction;

    private static final long serialVersionUID = 1L;
}
