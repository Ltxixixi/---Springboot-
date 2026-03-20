package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 偏好分析结果
 */
@Data
public class TourismPreferenceVO implements Serializable {

    private String userInputText;

    private Integer parsedDayCount;

    private BigDecimal parsedBudget;

    private String parsedSpotLocation;

    private List<String> parsedTagList;

    private String analysisSummary;

    private static final long serialVersionUID = 1L;
}
