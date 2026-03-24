package com.xiaobaitiao.springbootinit.service;

import com.xiaobaitiao.springbootinit.model.vo.SpotSimilarity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 协同过滤服务接口
 */
public interface SpotCollaborativeFilteringService {

    /**
     * 获取用户已交互的景点列表
     *
     * @param userId 用户ID
     * @return 景点ID集合
     */
    Set<Long> getUserInteractedSpotIds(Long userId);

    /**
     * 基于ItemCF（物品协同过滤）为用户推荐景点
     * 核心思想：用户喜欢景点A，景点A与景点B相似，则推荐景点B
     *
     * @param userId         用户ID
     * @param candidateSpotIds 候选景点ID列表（可为空）
     * @param topN           返回前N个推荐
     * @return 景点ID -> 推荐得分
     */
    Map<Long, Double> itemCfRecommend(Long userId, Set<Long> candidateSpotIds, int topN);

    /**
     * 基于UserCF（用户协同过滤）为用户推荐景点
     * 核心思想：找到与目标用户相似的用户，推荐相似用户喜欢的景点
     *
     * @param userId         用户ID
     * @param candidateSpotIds 候选景点ID列表（可为空）
     * @param topN           返回前N个推荐
     * @return 景点ID -> 推荐得分
     */
    Map<Long, Double> userCfRecommend(Long userId, Set<Long> candidateSpotIds, int topN);

    /**
     * 获取景点相似度列表
     *
     * @param spotId  景点ID
     * @param topN    返回前N个相似景点
     * @return 相似景点列表
     */
    List<SpotSimilarity> getSimilarSpots(Long spotId, int topN);

    /**
     * 获取所有景点的相似度矩阵（用于预计算缓存）
     *
     * @return 景点ID -> 相似景点列表
     */
    Map<Long, List<SpotSimilarity>> getSpotSimilarityMatrix();

    /**
     * 计算两个景点的相似度（余弦相似度）
     *
     * @param spotId1 景点1
     * @param spotId2 景点2
     * @return 相似度得分
     */
    Double calculateSpotSimilarity(Long spotId1, Long spotId2);

    /**
     * 刷新协同过滤模型（当交互数据变化时调用）
     */
    void refreshModel();

    /**
     * 获取用户-景点交互矩阵
     *
     * @return 用户ID -> (景点ID -> 交互得分)
     */
    Map<Long, Map<Long, Double>> getUserSpotMatrix();
}
