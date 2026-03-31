package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.mapper.UserSpotFavoritesMapper;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.service.ItemCFRecommendService;
import com.xiaobaitiao.springbootinit.service.SpotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基于物品的协同过滤推荐服务实现（ItemCF）
 *
 * 算法流程：
 * 1. 构建用户-景点交互矩阵（评分 1-5 分，收藏行为 +3 分）
 * 2. 使用余弦相似度计算景点间相似度
 * 3. 根据用户历史交互景点，计算目标景点推荐得分
 * 4. 返回得分最高的景点作为推荐结果
 */
@Service
@Slf4j
public class ItemCFRecommendServiceImpl implements ItemCFRecommendService {

    @Resource
    private SpotScoreMapper spotScoreMapper;

    @Resource
    private UserSpotFavoritesMapper userSpotFavoritesMapper;

    @Resource
    private SpotService spotService;

    /**
     * 景点相似度矩阵（缓存）
     * Key: "spotId1_spotId2"（小id在前），Value: 相似度（0-1）
     */
    private final Map<String, Double> similarityMatrix = new ConcurrentHashMap<>();

    /**
     * 景点列表（用于快速访问）
     */
    private volatile List<Spot> allSpots = new ArrayList<>();

    /**
     * 用户-景点交互矩阵
     * Key: userId, Value: Map<spotId, interactionScore>
     */
    private final Map<Long, Map<Long, Double>> userSpotInteractionMatrix = new ConcurrentHashMap<>();

    /**
     * 景点-用户倒排表（用于加速相似度计算）
     * Key: spotId, Value: Set<userId>
     */
    private final Map<Long, Set<Long>> spotUsersMap = new ConcurrentHashMap<>();

    /**
     * 相似度矩阵是否已过期
     */
    private volatile boolean matrixStale = true;

    /**
     * 相似度矩阵刷新间隔（分钟）
     */
    @Value("${recommend.itemcf.refresh-interval:30}")
    private int refreshIntervalMinutes;

    /**
     * 最小共现用户数（用于过滤噪声相似度）
     */
    private static final int MIN_CO_RATED_USERS = 2;

    /**
     * 相似度计算时用户评分偏好基准分
     */
    private static final double BASELINE_SCORE = 3.0;

    /**
     * 收藏行为权重（转换为等效评分）
     */
    private static final double FAVORITE_WEIGHT = 3.0;

    /**
     * 服务启动时初始化
     */
    @PostConstruct
    public void init() {
        log.info("ItemCF推荐服务初始化中...");
        refreshAllData();
        log.info("ItemCF推荐服务初始化完成，共加载 {} 个景点", allSpots.size());
    }

    /**
     * 每隔指定时间自动刷新（单位：毫秒）
     * 默认每30分钟刷新一次
     */
    @Scheduled(fixedRateString = "${recommend.itemcf.refresh-interval:1800000}")
    public void scheduledRefresh() {
        if (matrixStale) {
            refreshAllData();
        }
    }

    /**
     * 刷新所有数据
     */
    private synchronized void refreshAllData() {
        log.info("ItemCF开始刷新数据...");
        long startTime = System.currentTimeMillis();

        // 1. 加载所有景点
        allSpots = spotService.list();
        Set<Long> allSpotIds = allSpots.stream()
                .map(Spot::getId)
                .collect(Collectors.toSet());

        // 2. 加载所有评分数据
        List<SpotScore> allScores = spotScoreMapper.selectList(null);
        Map<Long, List<SpotScore>> spotScoresMap = allScores.stream()
                .filter(s -> s.getScore() != null)
                .collect(Collectors.groupingBy(SpotScore::getSpotId));

        // 3. 加载所有收藏数据
        List<UserSpotFavorites> allFavorites = userSpotFavoritesMapper.selectList(
                new QueryWrapper<UserSpotFavorites>().eq("status", 1)
        );

        // 4. 构建用户-景点交互矩阵
        buildUserSpotInteractionMatrix(allScores, allFavorites);

        // 5. 构建景点-用户倒排表
        buildSpotUsersMap(allScores, allFavorites);

        // 6. 计算景点相似度矩阵
        calculateSimilarityMatrix();

        matrixStale = false;
        long elapsedMs = System.currentTimeMillis() - startTime;
        log.info("ItemCF数据刷新完成，耗时 {} ms，景点数={}，评分数={}，收藏数={}",
                elapsedMs, allSpots.size(), allScores.size(), allFavorites.size());
    }

    /**
     * 构建用户-景点交互矩阵
     * 交互得分 = 评分分数（1-5）+ 收藏加成（收藏时+3）
     */
    private void buildUserSpotInteractionMatrix(List<SpotScore> allScores,
                                                List<UserSpotFavorites> allFavorites) {
        userSpotInteractionMatrix.clear();

        // 处理评分数据
        for (SpotScore score : allScores) {
            if (score.getUserId() == null || score.getSpotId() == null || score.getScore() == null) {
                continue;
            }
            userSpotInteractionMatrix
                    .computeIfAbsent(score.getUserId(), k -> new ConcurrentHashMap<>())
                    .merge(score.getSpotId(), score.getScore().doubleValue(), Double::sum);
        }

        // 处理收藏数据（收藏作为隐式正反馈，增加权重）
        for (UserSpotFavorites favorite : allFavorites) {
            if (favorite.getUserId() == null || favorite.getSpotId() == null) {
                continue;
            }
            userSpotInteractionMatrix
                    .computeIfAbsent(favorite.getUserId(), k -> new ConcurrentHashMap<>())
                    .merge(favorite.getSpotId(), FAVORITE_WEIGHT, Double::sum);
        }

        log.debug("用户-景点交互矩阵构建完成，共 {} 个用户", userSpotInteractionMatrix.size());
    }

    /**
     * 构建景点-用户倒排表
     */
    private void buildSpotUsersMap(List<SpotScore> allScores,
                                   List<UserSpotFavorites> allFavorites) {
        spotUsersMap.clear();

        // 从评分数据构建
        for (SpotScore score : allScores) {
            if (score.getUserId() != null && score.getSpotId() != null && score.getScore() != null) {
                spotUsersMap.computeIfAbsent(score.getSpotId(), k -> new HashSet<>())
                        .add(score.getUserId());
            }
        }

        // 从收藏数据构建
        for (UserSpotFavorites favorite : allFavorites) {
            if (favorite.getUserId() != null && favorite.getSpotId() != null) {
                spotUsersMap.computeIfAbsent(favorite.getSpotId(), k -> new HashSet<>())
                        .add(favorite.getUserId());
            }
        }

        log.debug("景点-用户倒排表构建完成，共 {} 个景点有交互记录", spotUsersMap.size());
    }

    /**
     * 计算景点相似度矩阵（余弦相似度）
     */
    private void calculateSimilarityMatrix() {
        similarityMatrix.clear();

        List<Long> spotIds = new ArrayList<>(spotUsersMap.keySet());
        int n = spotIds.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Long spotId1 = spotIds.get(i);
                Long spotId2 = spotIds.get(j);

                double similarity = calculateCosineSimilarity(spotId1, spotId2);
                if (similarity > 0) {
                    String key = buildSimilarityKey(spotId1, spotId2);
                    similarityMatrix.put(key, similarity);
                }
            }
        }

        log.info("景点相似度矩阵计算完成，共 {} 对景点存在相似关系", similarityMatrix.size());
    }

    /**
     * 计算两个景点的余弦相似度
     *
     * 余弦相似度公式：
     * sim(A,B) = (A · B) / (||A|| * ||B||)
     *          = Σ(r_ui * r_uj) / sqrt(Σr_ui²) * sqrt(Σr_uj²)
     *
     * 其中 r_ui 表示用户 i 对景点 A 的评分（已减去基准分）
     */
    private double calculateCosineSimilarity(Long spotId1, Long spotId2) {
        Set<Long> users1 = spotUsersMap.getOrDefault(spotId1, Collections.emptySet());
        Set<Long> users2 = spotUsersMap.getOrDefault(spotId2, Collections.emptySet());

        // 找出共同评价过这两个景点的用户
        Set<Long> coRatedUsers = new HashSet<>(users1);
        coRatedUsers.retainAll(users2);

        // 共现用户太少，返回0
        if (coRatedUsers.size() < MIN_CO_RATED_USERS) {
            return 0.0;
        }

        // 计算向量点积和模长
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (Long userId : coRatedUsers) {
            Map<Long, Double> userRatings = userSpotInteractionMatrix.get(userId);
            if (userRatings == null) continue;

            double ratingA = userRatings.getOrDefault(spotId1, 0.0) - BASELINE_SCORE;
            double ratingB = userRatings.getOrDefault(spotId2, 0.0) - BASELINE_SCORE;

            dotProduct += ratingA * ratingB;
            normA += ratingA * ratingA;
            normB += ratingB * ratingB;
        }

        // 避免除以零
        if (normA <= 0 || normB <= 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * 构建相似度矩阵的Key（小id在前）
     */
    private String buildSimilarityKey(Long spotId1, Long spotId2) {
        if (spotId1 < spotId2) {
            return spotId1 + "_" + spotId2;
        } else {
            return spotId2 + "_" + spotId1;
        }
    }

    /**
     * 获取两个景点间的相似度
     */
    @Override
    public double getSpotSimilarity(Long spotId1, Long spotId2) {
        if (spotId1 == null || spotId2 == null || spotId1.equals(spotId2)) {
            return 0.0;
        }
        String key = buildSimilarityKey(spotId1, spotId2);
        return similarityMatrix.getOrDefault(key, 0.0);
    }

    /**
     * 获取基于物品协同过滤的推荐景点
     *
     * 推荐得分公式：
     * Score(u, t) = Σ(sim(t, i) * r_ui) / Σ|sim(t, i)|
     *
     * 其中：
     * - t 是目标景点
     * - i 是用户历史喜欢的景点
     * - sim(t, i) 是景点t和i的相似度
     * - r_ui 是用户对景点i的评分
     */
    @Override
    public List<Long> getItemCFRecommendations(Long userId, int recommendSize) {
        // 如果用户未登录或无历史记录，返回空
        if (userId == null) {
            return Collections.emptyList();
        }

        Map<Long, Double> userRatings = userSpotInteractionMatrix.get(userId);
        if (CollUtil.isEmpty(userRatings)) {
            return Collections.emptyList();
        }

        // 获取用户已交互的景点
        Set<Long> interactedSpots = userRatings.keySet();

        // 获取所有景点ID
        Set<Long> allSpotIds = allSpots.stream()
                .map(Spot::getId)
                .filter(id -> !interactedSpots.contains(id)) // 排除已交互的
                .collect(Collectors.toSet());

        // 计算每个未交互景点的推荐得分
        Map<Long, Double> candidateScores = new HashMap<>();
        double totalSimilarityWeight = 0.0;

        for (Long candidateSpot : allSpotIds) {
            double score = 0.0;
            double weightSum = 0.0;

            for (Long likedSpot : interactedSpots) {
                double similarity = getSpotSimilarity(candidateSpot, likedSpot);
                if (similarity > 0) {
                    double rating = userRatings.get(likedSpot);
                    score += similarity * rating;
                    weightSum += Math.abs(similarity);
                }
            }

            if (weightSum > 0) {
                candidateScores.put(candidateSpot, score / weightSum);
                totalSimilarityWeight += weightSum;
            }
        }

        // 按得分降序排序
        return candidateScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(recommendSize)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取与指定景点相似的景点
     */
    @Override
    public List<Long> getSimilarSpots(Long spotId, int topN) {
        if (spotId == null) {
            return Collections.emptyList();
        }

        // 找出所有与目标景点相似的景点
        Map<Long, Double> similarities = new HashMap<>();

        for (Map.Entry<String, Double> entry : similarityMatrix.entrySet()) {
            String key = entry.getKey();
            double sim = entry.getValue();

            String[] parts = key.split("_");
            Long spotId1 = Long.parseLong(parts[0]);
            Long spotId2 = Long.parseLong(parts[1]);

            if (spotId1.equals(spotId)) {
                similarities.put(spotId2, sim);
            } else if (spotId2.equals(spotId)) {
                similarities.put(spotId1, sim);
            }
        }

        // 按相似度降序排序
        return similarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 刷新相似度矩阵（手动触发）
     */
    @Override
    public void refreshSimilarityMatrix() {
        matrixStale = true;
        refreshAllData();
        log.info("ItemCF相似度矩阵已手动刷新");
    }
}
