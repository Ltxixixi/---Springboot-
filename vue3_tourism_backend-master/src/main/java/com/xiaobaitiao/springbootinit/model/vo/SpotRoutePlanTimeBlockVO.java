package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单日路线中的时段分块
 */
@Data
public class SpotRoutePlanTimeBlockVO implements Serializable {

    /**
     * 时段标识：MORNING / AFTERNOON / EVENING
     */
    private String blockKey;

    /**
     * 时段名称
     */
    private String blockLabel;

    /**
     * 时段摘要
     */
    private String summary;

    /**
     * 时段内时间轴安排
     */
    private List<String> scheduleLineList;

    private static final long serialVersionUID = 1L;
}
