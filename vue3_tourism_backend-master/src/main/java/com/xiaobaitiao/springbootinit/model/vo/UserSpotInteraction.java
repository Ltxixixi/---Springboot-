package com.xiaobaitiao.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户-景点交互记录
 */
@Data
public class UserSpotInteraction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 景点ID
     */
    private Long spotId;

    /**
     * 交互得分（由多种行为加权计算得出）
     */
    private Double score;

    /**
     * 交互类型：view, favorite, score, order
     */
    private String interactionType;

    public UserSpotInteraction() {
    }

    public UserSpotInteraction(Long userId, Long spotId, Double score) {
        this.userId = userId;
        this.spotId = spotId;
        this.score = score;
    }

    public UserSpotInteraction(Long userId, Long spotId, Double score, String interactionType) {
        this.userId = userId;
        this.spotId = spotId;
        this.score = score;
        this.interactionType = interactionType;
    }
}
