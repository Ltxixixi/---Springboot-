package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobaitiao.springbootinit.mapper.SpotFeeMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotOrderMapper;
import com.xiaobaitiao.springbootinit.mapper.UserSpotFavoritesMapper;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotOrder;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.vo.SpotSimilarity;
import com.xiaobaitiao.springbootinit.model.vo.UserSpotInteraction;
import com.xiaobaitiao.springbootinit.service.SpotCollaborativeFilteringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 基于ItemCF和UserCF的协同过滤推荐服务实现
 *
 * 核心算法：
 * 1. ItemCF：基于物品的协同过滤，通过计算景点之间的相似度进行推荐
 * 2. UserCF：基于用户的协同过滤，通过找到相似用户进行推荐
 *
 * 论文支撑：符合开题报告中"基于用户行为数据构建用户—景点交互矩阵，使用协同过滤得到候选景点集合"
 */
@Service
@Slf4j
public class SpotCollaborativeFilteringServiceImpl implements SpotCollaborativeFilteringService {

    // ==================== 交互权重配置 ====================
    private static final double VIEW_WEIGHT = 1.0;           // 浏览权重
    private static final double FAVORITE_WEIGHT = 5.0;       // 收藏权重
    private static final double SCORE_WEIGHT = 4.0;          // 评分权重（满分5分制）
    private static final double ORDER_WEIGHT = 8.0;         // 订单权重

    // ==================== 相似度阈值 ====================
    private static final double SIMILARITY_THRESHOLD = 0.01; // 相似度阈值，低于此值不记录
    private static final int MAX_SIMILAR_SPOTS = 50;         // 每个景点最多保存的相似景点数

    // ==================== 缓存配置 ====================
    private volatile Map<Long, Map<Long, Double>> userSpotMatrix;      // 用户-景点交互矩阵
    private volatile Map<Long, List<SpotSimilarity>> spotSimilarityMatrix; // 景点相似度矩阵
    private volatile boolean modelInitialized = false;

    @Resource
    private SpotScoreMapper spotScoreMapper;

    @Resource
    private UserSpotFavoritesMapper userSpotFavoritesMapper;

    @Resource
    private SpotMapper spotMapper;

    @Resource
    private SpotFeeMapper spotFeeMapper;

    @Resource
    private SpotOrderMapper spotOrderMapper;

    /**
     * 启动时初始化模型
     */
    @PostConstruct
    public void init() {
        try {
            refreshModel();
            log.info("协同过滤模型初始化完成");
        } catch (Exception e) {
            log.error("协同过滤模型初始化失败", e);
        }
    }

    /**
     * 刷新协同过滤模型
     */
    @Override
    public synchronized void refreshModel() {
        log.info("开始刷新协同过滤模型...");
        long startTime = System.currentTimeMillis();

        // 构建用户-景点交互矩阵
        this.userSpotMatrix = buildUserSpotMatrix();

        // 构建景点相似度矩阵
        this.spotSimilarityMatrix = buildSpotSimilarityMatrix();

        this.modelInitialized = true;

        long costTime = System.currentTimeMillis() - startTime;
        log.info("协同过滤模型刷新完成，耗时: {}ms", costTime);
    }

    /**
     * 构建用户-景点交互矩阵
     * 矩阵内容：用户对景点的综合交互得分
     */
    private Map<Long, Map<Long, Double>> buildUserSpotMatrix() {
        Map<Long, Map<Long, Double>> matrix = new ConcurrentHashMap<>();

        // 1. 从评分表获取评分数据
        List<SpotScore> scoreList = spotScoreMapper.selectList(
            new QueryWrapper<SpotScore>().eq("isDelete", 0)
        );

        for (SpotScore score : scoreList) {
            if (score.getUserId() == null || score.getSpotId() == null || score.getScore() == null) {
                continue;
            }
            // 评分转换为权重分：(score - 3) * SCORE_WEIGHT，低于3分不计入
            double weight = (score.getScore() - 3) * SCORE_WEIGHT;
            if (weight > 0) {
                matrix.computeIfAbsent(score.getUserId(), k -> new ConcurrentHashMap<>())
                      .merge(score.getSpotId(), weight, Double::sum);
            }
        }

        // 2. 从收藏表获取收藏数据
        List<UserSpotFavorites> favoriteList = userSpotFavoritesMapper.selectList(
            new QueryWrapper<UserSpotFavorites>().eq("status", 1).eq("isDelete", 0)
        );

        for (UserSpotFavorites favorite : favoriteList) {
            if (favorite.getUserId() == null || favorite.getSpotId() == null) {
                continue;
            }
            matrix.computeIfAbsent(favorite.getUserId(), k -> new ConcurrentHashMap<>())
                  .merge(favorite.getSpotId(), FAVORITE_WEIGHT, Double::sum);
        }

        // 3. 从订单表获取购票数据
        List<SpotOrder> orderList = getPaidOrders();
        for (SpotOrder order : orderList) {
            if (order.getUserId() == null || order.getSpotFeeId() == null) {
                continue;
            }
            // 需要获取订单对应的景点ID
            SpotFee spotFee = spotFeeMapper.selectById(order.getSpotFeeId());
            if (spotFee != null && spotFee.getSpotId() != null) {
                matrix.computeIfAbsent(order.getUserId(), k -> new ConcurrentHashMap<>())
                      .merge(spotFee.getSpotId(), ORDER_WEIGHT, Double::sum);
            }
        }

        log.info("用户-景点交互矩阵构建完成，共 {} 个用户", matrix.size());
        return matrix;
    }

    /**
     * 获取已支付的订单列表
     */
    private List<SpotOrder> getPaidOrders() {
        try {
            QueryWrapper<SpotOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("payStatus", 1).eq("isDelete", 0);
            return spotOrderMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.warn("查询已支付订单失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 构建景点相似度矩阵（基于ItemCF）
     * 使用余弦相似度计算两个景点之间的相似度
     *
     * 公式：sim(i,j) = Σ(u_i * u_j) / (||u_i|| * ||u_j||)
     * 其中 u_i 和 u_j 是用户对景点i和j的评分向量
     */
    private Map<Long, List<SpotSimilarity>> buildSpotSimilarityMatrix() {
        Map<Long, List<SpotSimilarity>> similarityMatrix = new ConcurrentHashMap<>();

        if (userSpotMatrix == null || userSpotMatrix.isEmpty()) {
            log.warn("用户-景点交互矩阵为空，无法构建相似度矩阵");
            return similarityMatrix;
        }

        // 获取所有景点ID
        Set<Long> allSpotIds = new HashSet<>();
        for (Map<Long, Double> userRatings : userSpotMatrix.values()) {
            allSpotIds.addAll(userRatings.keySet());
        }

        List<Long> spotIdList = new ArrayList<>(allSpotIds);
        int total = spotIdList.size();
        int processed = 0;

        log.info("开始计算 {} 个景点之间的相似度", total);

        // 计算景点两两之间的相似度
        for (int i = 0; i < spotIdList.size(); i++) {
            Long spotId1 = spotIdList.get(i);

            // 获取对景点1有交互的用户评分
            Map<Long, Double> ratingVector1 = getSpotRatingVector(spotId1);
            if (ratingVector1.isEmpty()) {
                continue;
            }

            for (int j = i + 1; j < spotIdList.size(); j++) {
                Long spotId2 = spotIdList.get(j);

                // 获取对景点2有交互的用户评分
                Map<Long, Double> ratingVector2 = getSpotRatingVector(spotId2);
                if (ratingVector2.isEmpty()) {
                    continue;
                }

                // 计算余弦相似度
                double similarity = cosineSimilarity(ratingVector1, ratingVector2);

                if (similarity > SIMILARITY_THRESHOLD) {
                    // 双向存储
                    similarityMatrix.computeIfAbsent(spotId1, k -> new CopyOnWriteArrayList<>())
                            .add(new SpotSimilarity(spotId2, similarity));
                    similarityMatrix.computeIfAbsent(spotId2, k -> new CopyOnWriteArrayList<>())
                            .add(new SpotSimilarity(spotId1, similarity));
                }
            }

            processed++;
            if (processed % 10 == 0) {
                log.debug("相似度计算进度: {}/{}", processed, total);
            }
        }

        // 对每个景点的相似景点列表进行排序，并限制数量
        for (Map.Entry<Long, List<SpotSimilarity>> entry : similarityMatrix.entrySet()) {
            List<SpotSimilarity> sortedList = entry.getValue().stream()
                    .sorted()
                    .limit(MAX_SIMILAR_SPOTS)
                    .collect(Collectors.toList());
            entry.setValue(sortedList);
        }

        log.info("景点相似度矩阵构建完成，共 {} 个景点有相似景点", similarityMatrix.size());
        return similarityMatrix;
    }

    /**
     * 获取某个景点的用户评分向量
     */
    private Map<Long, Double> getSpotRatingVector(Long spotId) {
        Map<Long, Double> vector = new HashMap<>();
        for (Map.Entry<Long, Map<Long, Double>> entry : userSpotMatrix.entrySet()) {
            Double rating = entry.getValue().get(spotId);
            if (rating != null && rating > 0) {
                vector.put(entry.getKey(), rating);
            }
        }
        return vector;
    }

    /**
     * 计算两个向量的余弦相似度
     */
    private double cosineSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        if (vector1.isEmpty() || vector2.isEmpty()) {
            return 0.0;
        }

        // 找共同的用户
        Set<Long> commonUsers = new HashSet<>(vector1.keySet());
        commonUsers.retainAll(vector2.keySet());

        if (commonUsers.isEmpty()) {
            return 0.0;
        }

        // 计算点积
        double dotProduct = 0.0;
        for (Long userId : commonUsers) {
            dotProduct += vector1.get(userId) * vector2.get(userId);
        }

        // 计算模长
        double norm1 = 0.0;
        for (Double rating : vector1.values()) {
            norm1 += rating * rating;
        }
        norm1 = Math.sqrt(norm1);

        double norm2 = 0.0;
        for (Double rating : vector2.values()) {
            norm2 += rating * rating;
        }
        norm2 = Math.sqrt(norm2);

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (norm1 * norm2);
    }

    /**
     * 获取用户已交互的景点ID集合
     */
    @Override
    public Set<Long> getUserInteractedSpotIds(Long userId) {
        if (userSpotMatrix == null || !userSpotMatrix.containsKey(userId)) {
            return Collections.emptySet();
        }
        return userSpotMatrix.get(userId).keySet();
    }

    /**
     * ItemCF推荐算法
     * 思想：用户u喜欢景点i，景点i与景点j相似，则用户u也可能喜欢景点j
     *
     * @param userId 用户ID
     * @param candidateSpotIds 候选景点集合（用于排除已交互景点）
     * @param topN 返回前N个推荐
     * @return 景点ID -> 推荐得分
     */
    @Override
    public Map<Long, Double> itemCfRecommend(Long userId, Set<Long> candidateSpotIds, int topN) {
        Map<Long, Double> recommendationScores = new HashMap<>();

        if (spotSimilarityMatrix == null || !userSpotMatrix.containsKey(userId)) {
            return recommendationScores;
        }

        // 获取用户已交互的景点
        Map<Long, Double> userRatings = userSpotMatrix.get(userId);

        // 对每个已交互景点，找到相似景点
        for (Map.Entry<Long, Double> entry : userRatings.entrySet()) {
            Long likedSpotId = entry.getKey();
            Double userRating = entry.getValue();

            // 获取相似景点列表
            List<SpotSimilarity> similarSpots = spotSimilarityMatrix.get(likedSpotId);
            if (CollUtil.isEmpty(similarSpots)) {
                continue;
            }

            for (SpotSimilarity similarSpot : similarSpots) {
                Long candidateSpotId = similarSpot.getSpotId();

                // 排除已交互的景点
                if (userRatings.containsKey(candidateSpotId)) {
                    continue;
                }

                // 如果有候选集合限制，检查是否在候选集合中
                if (candidateSpotIds != null && !candidateSpotIds.contains(candidateSpotId)) {
                    continue;
                }

                // 得分 = 用户对原景点的评分 * 景点相似度
                double score = userRating * similarSpot.getSimilarity();
                recommendationScores.merge(candidateSpotId, score, Double::sum);
            }
        }

        // 返回TopN
        return getTopNRecommendations(recommendationScores, topN);
    }

    /**
     * UserCF推荐算法
     * 思想：找到与目标用户相似的用户群，推荐相似用户喜欢的景点
     *
     * @param userId 用户ID
     * @param candidateSpotIds 候选景点集合
     * @param topN 返回前N个推荐
     * @return 景点ID -> 推荐得分
     */
    @Override
    public Map<Long, Double> userCfRecommend(Long userId, Set<Long> candidateSpotIds, int topN) {
        Map<Long, Double> recommendationScores = new HashMap<>();

        if (userSpotMatrix == null || !userSpotMatrix.containsKey(userId)) {
            return recommendationScores;
        }

        Map<Long, Double> targetUserRatings = userSpotMatrix.get(userId);

        // 1. 找到相似用户
        Map<Long, Double> userSimilarities = new HashMap<>();
        for (Map.Entry<Long, Map<Long, Double>> entry : userSpotMatrix.entrySet()) {
            Long otherUserId = entry.getKey();
            if (otherUserId.equals(userId)) {
                continue;
            }

            Map<Long, Double> otherUserRatings = entry.getValue();
            double similarity = cosineSimilarity(targetUserRatings, otherUserRatings);

            if (similarity > SIMILARITY_THRESHOLD) {
                userSimilarities.put(otherUserId, similarity);
            }
        }

        // 2. 根据相似用户的评分，推荐目标用户未交互的景点
        for (Map.Entry<Long, Double> userEntry : userSimilarities.entrySet()) {
            Long similarUserId = userEntry.getKey();
            Double userSimilarity = userEntry.getValue();

            Map<Long, Double> similarUserRatings = userSpotMatrix.get(similarUserId);
            for (Map.Entry<Long, Double> ratingEntry : similarUserRatings.entrySet()) {
                Long spotId = ratingEntry.getKey();
                Double rating = ratingEntry.getValue();

                // 排除已交互的景点
                if (targetUserRatings.containsKey(spotId)) {
                    continue;
                }

                // 如果有候选集合限制，检查是否在候选集合中
                if (candidateSpotIds != null && !candidateSpotIds.contains(spotId)) {
                    continue;
                }

                // 得分 = 用户相似度 * 相似用户对该景点的评分
                double score = userSimilarity * rating;
                recommendationScores.merge(spotId, score, Double::sum);
            }
        }

        // 返回TopN
        return getTopNRecommendations(recommendationScores, topN);
    }

    /**
     * 获取TopN推荐结果
     */
    private Map<Long, Double> getTopNRecommendations(Map<Long, Double> scores, int topN) {
        return scores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * 获取景点的相似景点列表
     */
    @Override
    public List<SpotSimilarity> getSimilarSpots(Long spotId, int topN) {
        if (spotSimilarityMatrix == null || !spotSimilarityMatrix.containsKey(spotId)) {
            return Collections.emptyList();
        }
        return spotSimilarityMatrix.get(spotId).stream()
                .sorted()
                .limit(topN)
                .collect(Collectors.toList());
    }

    /**
     * 获取景点相似度矩阵
     */
    @Override
    public Map<Long, List<SpotSimilarity>> getSpotSimilarityMatrix() {
        if (!modelInitialized) {
            refreshModel();
        }
        return spotSimilarityMatrix;
    }

    /**
     * 计算两个景点的相似度
     */
    @Override
    public Double calculateSpotSimilarity(Long spotId1, Long spotId2) {
        if (spotSimilarityMatrix == null) {
            return 0.0;
        }

        List<SpotSimilarity> similarSpots = spotSimilarityMatrix.get(spotId1);
        if (CollUtil.isEmpty(similarSpots)) {
            return 0.0;
        }

        return similarSpots.stream()
                .filter(s -> s.getSpotId().equals(spotId2))
                .findFirst()
                .map(SpotSimilarity::getSimilarity)
                .orElse(0.0);
    }

    /**
     * 获取用户-景点交互矩阵
     */
    @Override
    public Map<Long, Map<Long, Double>> getUserSpotMatrix() {
        if (!modelInitialized) {
            refreshModel();
        }
        return userSpotMatrix;
    }
}
