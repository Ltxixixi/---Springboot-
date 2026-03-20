package com.xiaobaitiao.springbootinit.service.agent.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 多智能协作上下文
 */
@Data
public class TourismAgentContext {

    private String userInputText;

    private Integer dayCount;

    private BigDecimal budget;

    private String spotLocation;

    private List<String> preferredTagList;

    private Integer recommendSize;

    private String preferenceSummary;
}
