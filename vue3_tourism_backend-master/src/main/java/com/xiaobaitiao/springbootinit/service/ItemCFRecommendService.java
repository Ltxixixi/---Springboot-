package com.xiaobaitiao.springbootinit.service;

/**
 * 基于物品的协同过滤推荐服务（ItemCF）
 *
 * 算法原理：
 * 1. 构建用户-景点交互矩阵（评分 + 收藏行为）
 * 2. 计算景点间的余弦相似度
 * 3. 根据用户历史喜欢的景点，推荐相似景点
 */
public interface ItemCFRecommendService {

    /**
     * 获取基于物品协同过滤的推荐景点
     *
     * @param userId      用户ID（可为null，表示未登录用户）
     * @param recommendSize 推荐数量
     * @return 推荐景点ID列表，按得分降序
     */
    java.util.List<Long> getItemCFRecommendations(Long userId, int recommendSize);

    /**
     * 获取与指定景点相似的景点
     *
     * @param spotId      目标景点ID
     * @param topN        返回相似景点数量
     * @return 相似景点ID列表
     */
    java.util.List<Long> getSimilarSpots(Long spotId, int topN);

    /**
     * 获取两个景点间的相似度
     *
     * @param spotId1 景点1
     * @param spotId2 景点2
     * @return 相似度得分（0-1之间）
     */
    double getSpotSimilarity(Long spotId1, Long spotId2);

    /**
     * 刷新相似度矩阵（当有新的评分/收藏时调用）
     */
    void refreshSimilarityMatrix();
}
