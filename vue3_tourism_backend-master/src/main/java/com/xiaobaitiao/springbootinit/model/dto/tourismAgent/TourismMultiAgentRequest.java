package com.xiaobaitiao.springbootinit.model.dto.tourismAgent;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 多智能协作旅游规划请求
 */
@Data
public class TourismMultiAgentRequest implements Serializable {

    /**
     * 用户输入
     */
    private String userInputText;

    /**
     * 游玩天数
     */
    private Integer dayCount;

    /**
     * 预算
     */
    private BigDecimal budget;

    /**
     * 目标地区
     */
    private String spotLocation;

    /**
     * 偏好标签
     */
    private List<String> preferredTagList;

    /**
     * 推荐数量
     */
    private Integer recommendSize;

    private static final long serialVersionUID = 1L;
}
