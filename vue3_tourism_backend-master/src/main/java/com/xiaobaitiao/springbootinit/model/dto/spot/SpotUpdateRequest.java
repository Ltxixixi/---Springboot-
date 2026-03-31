package com.xiaobaitiao.springbootinit.model.dto.spot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 更新景点表请求
 *
 * @author toxi
 * 
 */
@Data
public class SpotUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * adminId
     */
    private Long adminId;
    /**
     * 景点名
     */
    private String spotName;

    /**
     * 景点封面图
     */
    private String spotAvatar;

    /**
     * 景点所在地
     */
    private String spotLocation;
    /**
     * 景点介绍
     */
    private String spotDescription;

    /**
     * 景点纬度
     */
    private BigDecimal latitude;

    /**
     * 景点经度
     */
    private BigDecimal longitude;

    /**
     * 建议游玩时长（分钟）
     */
    private Integer visitDurationMinutes;

    /**
     * 开放时间，格式 HH:mm
     */
    private String openTime;

    /**
     * 关闭时间，格式 HH:mm
     */
    private String closeTime;

    /**
     * 景点标签（JSON字符串数组）
     */
    private List<String> spotTagList;
    /**
     * 收藏量
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 景点状态（1开放，0关闭，默认关闭）
     */
    private Integer spotStatus;

    private static final long serialVersionUID = 1L;
}
