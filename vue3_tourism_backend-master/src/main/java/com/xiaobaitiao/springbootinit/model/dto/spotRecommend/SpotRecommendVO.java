package com.xiaobaitiao.springbootinit.model.dto.spotRecommend;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 景点推荐结果
 */
@Data
public class SpotRecommendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 推荐景点列表
     */
    private List<RecommendedSpot> spotList;

    /**
     * 推荐总数
     */
    private Integer totalCount;

    /**
     * 推荐算法说明
     */
    private String algorithmInfo;

    /**
     * 推荐策略类型
     */
    private String strategyType;

    /**
     * 单个推荐景点
     */
    @Data
    public static class RecommendedSpot implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 景点ID
         */
        private Long spotId;

        /**
         * 景点名称
         */
        private String spotName;

        /**
         * 综合推荐得分
         */
        private Double totalScore;

        /**
         * 协同过滤得分
         */
        private Double cfScore;

        /**
         * 内容特征得分
         */
        private Double contentScore;

        /**
         * 热度得分
         */
        private Double hotScore;

        /**
         * 推荐理由
         */
        private String reason;

        /**
         * 推荐来源：cf(协同过滤), tag(标签匹配), hot(热门), hybrid(混合)
         */
        private String source;
    }
}
