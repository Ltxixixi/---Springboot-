package com.xiaobaitiao.springbootinit.model.dto.spotRecommend;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 景点推荐请求参数
 */
@Data
public class SpotRecommendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 目标用户ID（可选，不传则为未登录用户，返回热门推荐）
     */
    private Long userId;

    /**
     * 目标城市/地区
     */
    private String city;

    /**
     * 推荐数量
     */
    private Integer size;

    /**
     * 用户偏好标签列表
     */
    private List<String> preferredTags;

    /**
     * 用户预算（单景点门票上限）
     */
    private BigDecimal budget;

    /**
     * 人群类型：family(亲子), elder(长辈), couple(情侣), business(商务), solo(独行)
     */
    private String crowdType;

    /**
     * 推荐模式：hot(热门), personalized(个性化), hybrid(混合)
     */
    private String recommendMode;

    /**
     * 是否需要可解释理由
     */
    private Boolean needReason;
}
