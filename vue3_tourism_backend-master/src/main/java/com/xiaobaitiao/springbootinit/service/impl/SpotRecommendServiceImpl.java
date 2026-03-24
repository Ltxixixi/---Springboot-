package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotFeeMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.mapper.UserSpotFavoritesMapper;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.vo.SpotSimilarity;
import com.xiaobaitiao.springbootinit.model.dto.spotRecommend.SpotRecommendRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRecommend.SpotRecommendVO;
import com.xiaobaitiao.springbootinit.service.SpotCollaborativeFilteringService;
import com.xiaobaitiao.springbootinit.service.SpotRecommendService;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 混合推荐服务实现
 *
 * 采用"召回 + 排序 + 重排"架构：
 * 1. 召回层：ItemCF + 城市召回 + 标签召回 + 热门召回
 * 2. 排序层：多特征加权打分
 * 3. 重排层：多样性控制
 *
 * 论文支撑：
 * - "基于协同过滤与内容特征融合的混合推荐算法"
 * - "融合协同过滤得分、标签匹配度、热度指数等特征进行加权排序"
 */
@Service
@Slf4j
public class SpotRecommendServiceImpl implements SpotRecommendService {

    // ==================== 排序权重配置 ====================
    private static final double CF_WEIGHT = 0.25;          // 协同过滤权重
    private static final double TAG_WEIGHT = 0.25;        // 标签匹配权重
    private static final double HOT_WEIGHT = 0.25;         // 热度权重
    private static final double BUDGET_WEIGHT = 0.15;      // 预算匹配权重
    private static final double CROWD_WEIGHT = 0.10;       // 人群适配权重

    // ==================== 召回配置 ====================
    private static final int RECALL_SIZE = 100;            // 召回候选集大小
    private static final int RECOMMEND_SIZE_DEFAULT = 10;  // 默认推荐数量

    // ==================== 多样性配置 ====================
    private static final int MAX_SAME_TAG_SPOTS = 3;       // 同标签最多景点数
    private static final int MAX_SAME_LOCATION_SPOTS = 4;  // 同地区最多景点数

    // ==================== 缓存数据 ====================
    private Map<Long, BigDecimal> minPriceCache;
    private Map<Long, Double> avgScoreCache;
    private Map<Long, Spot> spotCache;

    @Resource
    private SpotMapper spotMapper;

    @Resource
    private SpotFeeMapper spotFeeMapper;

    @Resource
    private SpotScoreMapper spotScoreMapper;

    @Resource
    private UserSpotFavoritesMapper userSpotFavoritesMapper;

    @Resource
    private SpotService spotService;

    @Resource
    private UserService userService;

    @Resource
    private SpotCollaborativeFilteringService cfService;

    @PostConstruct
    public void init() {
        refreshCache();
    }

    /**
     * 刷新缓存数据
     */
    private void refreshCache() {
        log.info("开始刷新推荐系统缓存...");

        // 构建最低价格缓存
        this.minPriceCache = buildMinPriceCache();

        // 构建平均评分缓存
        this.avgScoreCache = buildAvgScoreCache();

        // 构建景点缓存
        List<Spot> allSpots = spotService.list();
        this.spotCache = allSpots.stream()
                .collect(Collectors.toMap(Spot::getId, s -> s, (a, b) -> a));

        log.info("推荐系统缓存刷新完成，共缓存 {} 个景点", spotCache.size());
    }

    @Override
    public SpotRecommendVO getRecommendations(SpotRecommendRequest request, HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        int recommendSize = request.getSize() != null && request.getSize() > 0
                ? Math.min(request.getSize(), 20)
                : RECOMMEND_SIZE_DEFAULT;

        // 1. 获取目标用户ID
        Long userId = request.getUserId();
        if (userId == null) {
            User loginUser = userService.getLoginUserPermitNull(httpRequest);
            if (loginUser != null) {
                userId = loginUser.getId();
            }
        }

        // 2. 获取所有开放景点
        List<Spot> openSpots = spotService.list(
                new QueryWrapper<Spot>().eq("spotStatus", 1)
        );

        if (CollUtil.isEmpty(openSpots)) {
            return buildEmptyResult(recommendSize, "暂无开放景点");
        }

        // 3. 召回阶段：多路召回
        Set<Long> recallSet = multiChannelRecall(userId, request, openSpots);

        // 4. 排序阶段：加权打分
        List<RecommendCandidate> candidates = rankingCandidates(userId, request, recallSet);

        // 5. 重排阶段：多样性控制
        List<RecommendCandidate> finalCandidates = rerankCandidates(candidates, recommendSize);

        // 6. 构建返回结果
        return buildRecommendResult(finalCandidates, request);
    }

    @Override
    public void refreshRecommendModel() {
        // 刷新协同过滤模型
        cfService.refreshModel();
        // 刷新缓存
        refreshCache();
    }

    /**
     * 多路召回
     * - ItemCF协同过滤召回
     * - 城市召回
     * - 标签召回
     * - 热门召回
     */
    private Set<Long> multiChannelRecall(Long userId, SpotRecommendRequest request, List<Spot> openSpots) {
        Set<Long> recallSet = new LinkedHashSet<>();

        // 1. 协同过滤召回
        if (userId != null) {
            Map<Long, Double> cfRecommendations = cfService.itemCfRecommend(userId, null, RECALL_SIZE);
            recallSet.addAll(cfRecommendations.keySet());
            log.debug("ItemCF召回: {} 个景点", cfRecommendations.size());
        }

        // 2. 城市召回
        String city = request.getCity();
        if (StringUtils.isNotBlank(city)) {
            List<Long> citySpotIds = openSpots.stream()
                    .filter(spot -> StringUtils.containsIgnoreCase(
                            StringUtils.defaultString(spot.getSpotLocation()), city))
                    .map(Spot::getId)
                    .collect(Collectors.toList());
            recallSet.addAll(citySpotIds);
            log.debug("城市召回: {} 个景点", citySpotIds.size());
        }

        // 3. 标签召回
        List<String> tags = request.getPreferredTags();
        if (CollUtil.isNotEmpty(tags)) {
            Set<String> tagSetLower = tags.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
            List<Long> tagSpotIds = openSpots.stream()
                    .filter(spot -> {
                        List<String> spotTags = parseSpotTags(spot.getSpotTags());
                        return spotTags.stream().anyMatch(tag ->
                                tagSetLower.contains(tag.toLowerCase()));
                    })
                    .map(Spot::getId)
                    .collect(Collectors.toList());
            recallSet.addAll(tagSpotIds);
            log.debug("标签召回: {} 个景点", tagSpotIds.size());
        }

        // 4. 热门召回（兜底）
        if (recallSet.size() < RECALL_SIZE) {
            List<Long> hotSpotIds = openSpots.stream()
                    .sorted(Comparator.comparingInt((Spot s) ->
                            ObjectUtils.isEmpty(s.getViewNum()) ? 0 : s.getViewNum()).reversed())
                    .limit(RECALL_SIZE)
                    .map(Spot::getId)
                    .collect(Collectors.toList());
            recallSet.addAll(hotSpotIds);
            log.debug("热门召回: {} 个景点", hotSpotIds.size());
        }

        log.info("多路召回完成，共召回 {} 个候选景点", recallSet.size());
        return recallSet;
    }

    /**
     * 排序阶段：对候选景点进行多特征加权打分
     *
     * Score = w1*CF + w2*TagMatch + w3*HotScore + w4*BudgetFit + w5*CrowdFit
     */
    private List<RecommendCandidate> rankingCandidates(Long userId, SpotRecommendRequest request, Set<Long> recallSet) {
        List<RecommendCandidate> candidates = new ArrayList<>();

        // 获取用户偏好标签
        Map<String, Double> userTagPreference = buildUserTagPreference(userId, request);

        // 获取协同过滤得分
        Map<Long, Double> cfScores = new HashMap<>();
        if (userId != null) {
            cfScores = cfService.itemCfRecommend(userId, recallSet, recallSet.size());
        }

        // 计算归一化因子
        double maxViewNum = spotCache.values().stream()
                .mapToDouble(s -> ObjectUtils.isEmpty(s.getViewNum()) ? 0 : s.getViewNum())
                .max().orElse(1);
        double maxFavourNum = spotCache.values().stream()
                .mapToDouble(s -> ObjectUtils.isEmpty(s.getFavourNum()) ? 0 : s.getFavourNum())
                .max().orElse(1);

        // 对每个候选景点打分
        for (Long spotId : recallSet) {
            Spot spot = spotCache.get(spotId);
            if (spot == null) continue;

            // CF得分
            double cfScore = cfScores.getOrDefault(spotId, 0.0);

            // 标签匹配得分
            List<String> spotTags = parseSpotTags(spot.getSpotTags());
            double tagScore = calculateTagScore(spotTags, userTagPreference);

            // 热度得分
            double hotScore = calculateHotScore(spot, avgScoreCache.get(spotId), maxViewNum, maxFavourNum);

            // 预算匹配得分
            BigDecimal budget = request.getBudget();
            double budgetScore = calculateBudgetScore(spotId, budget);

            // 人群适配得分
            String crowdType = request.getCrowdType();
            double crowdScore = calculateCrowdScore(spotTags, crowdType);

            // 综合得分
            double totalScore = CF_WEIGHT * normalizeCfScore(cfScore)
                    + TAG_WEIGHT * tagScore
                    + HOT_WEIGHT * hotScore
                    + BUDGET_WEIGHT * budgetScore
                    + CROWD_WEIGHT * crowdScore;

            candidates.add(new RecommendCandidate(spot, totalScore, cfScore, tagScore, hotScore,
                    buildRecommendReason(spot, spotTags, userTagPreference, cfScore, crowdType)));

            // 排除用户已交互的景点
            if (userId != null) {
                Set<Long> interacted = cfService.getUserInteractedSpotIds(userId);
                if (interacted.contains(spotId)) {
                    candidates.get(candidates.size() - 1).setExcluded(true);
                }
            }
        }

        // 排序
        return candidates.stream()
                .filter(c -> !c.isExcluded())
                .sorted(Comparator.comparingDouble(RecommendCandidate::getTotalScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 重排阶段：多样性控制
     * - 控制同标签景点数量
     * - 控制同区域景点数量
     */
    private List<RecommendCandidate> rerankCandidates(List<RecommendCandidate> candidates, int targetSize) {
        List<RecommendCandidate> result = new ArrayList<>();
        Map<String, Integer> tagCount = new HashMap<>();
        Map<String, Integer> locationCount = new HashMap<>();

        for (RecommendCandidate candidate : candidates) {
            if (result.size() >= targetSize) break;

            Spot spot = candidate.getSpot();
            List<String> spotTags = parseSpotTags(spot.getSpotTags());
            String location = StringUtils.defaultString(spot.getSpotLocation());

            // 检查多样性约束
            boolean canAdd = true;

            // 同标签约束
            for (String tag : spotTags) {
                int count = tagCount.getOrDefault(tag, 0);
                if (count >= MAX_SAME_TAG_SPOTS) {
                    canAdd = false;
                    break;
                }
            }

            // 同区域约束
            if (canAdd) {
                int locCount = locationCount.getOrDefault(location, 0);
                if (locCount >= MAX_SAME_LOCATION_SPOTS) {
                    canAdd = false;
                }
            }

            if (canAdd) {
                result.add(candidate);
                // 更新计数
                for (String tag : spotTags) {
                    tagCount.merge(tag, 1, Integer::sum);
                }
                locationCount.merge(location, 1, Integer::sum);
            }
        }

        // 如果重排后数量不足，补充剩余候选
        if (result.size() < targetSize) {
            for (RecommendCandidate candidate : candidates) {
                if (result.size() >= targetSize) break;
                if (!result.contains(candidate)) {
                    result.add(candidate);
                }
            }
        }

        return result;
    }

    /**
     * 构建推荐理由
     */
    private String buildRecommendReason(Spot spot, List<String> spotTags,
                                        Map<String, Double> userTagPreference,
                                        double cfScore, String crowdType) {
        List<String> reasons = new ArrayList<>();

        // 基于标签偏好
        List<String> matchedTags = spotTags.stream()
                .filter(tag -> userTagPreference.getOrDefault(tag, 0.0) > 0)
                .limit(2)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(matchedTags)) {
            reasons.add("符合你的" + String.join("、", matchedTags) + "偏好");
        }

        // 基于协同过滤
        if (cfScore > 0) {
            reasons.add("与你喜欢的景点风格相似");
        }

        // 基于评分
        Double avgScore = avgScoreCache.get(spot.getId());
        if (avgScore != null && avgScore >= 4.5) {
            reasons.add("用户评分较高");
        }

        // 基于人群
        if (StringUtils.isNotBlank(crowdType)) {
            if ("family".equalsIgnoreCase(crowdType) && containsAnyTag(spotTags, "亲子", "儿童", "家庭")) {
                reasons.add("适合亲子游玩");
            } else if ("elder".equalsIgnoreCase(crowdType) && containsAnyTag(spotTags, "轻松", "自然", "公园")) {
                reasons.add("适合长辈休闲");
            } else if ("couple".equalsIgnoreCase(crowdType) && containsAnyTag(spotTags, "浪漫", "夜景", "景观")) {
                reasons.add("适合情侣约会");
            }
        }

        return CollUtil.isNotEmpty(reasons) ? String.join("，", reasons) : "热门景点推荐";
    }

    private boolean containsAnyTag(List<String> spotTags, String... keywords) {
        for (String tag : spotTags) {
            for (String keyword : keywords) {
                if (StringUtils.containsIgnoreCase(tag, keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 构建用户标签偏好
     */
    private Map<String, Double> buildUserTagPreference(Long userId, SpotRecommendRequest request) {
        Map<String, Double> preference = new HashMap<>();

        // 1. 从请求参数获取偏好标签
        if (CollUtil.isNotEmpty(request.getPreferredTags())) {
            for (String tag : request.getPreferredTags()) {
                preference.merge(tag, 5.0, Double::sum);
            }
        }

        // 2. 从用户收藏获取偏好
        if (userId != null) {
            List<UserSpotFavorites> favorites = userSpotFavoritesMapper.selectList(
                    new QueryWrapper<UserSpotFavorites>()
                            .eq("userId", userId)
                            .eq("status", 1)
                            .eq("isDelete", 0)
            );

            for (UserSpotFavorites fav : favorites) {
                Spot spot = spotCache.get(fav.getSpotId());
                if (spot != null) {
                    for (String tag : parseSpotTags(spot.getSpotTags())) {
                        preference.merge(tag, 3.0, Double::sum);
                    }
                }
            }

            // 3. 从用户评分获取偏好
            List<SpotScore> scores = spotScoreMapper.selectList(
                    new QueryWrapper<SpotScore>()
                            .eq("userId", userId)
                            .eq("isDelete", 0)
            );

            for (SpotScore score : scores) {
                Spot spot = spotCache.get(score.getSpotId());
                if (spot != null && score.getScore() != null) {
                    double weight = (score.getScore() - 3) * 1.5;
                    if (weight > 0) {
                        for (String tag : parseSpotTags(spot.getSpotTags())) {
                            preference.merge(tag, weight, Double::sum);
                        }
                    }
                }
            }
        }

        return preference;
    }

    /**
     * 计算标签匹配得分
     */
    private double calculateTagScore(List<String> spotTags, Map<String, Double> userPreference) {
        if (CollUtil.isEmpty(spotTags) || userPreference.isEmpty()) {
            return 0.5; // 无偏好时给中等分
        }

        double totalScore = 0.0;
        int matchCount = 0;

        for (String tag : spotTags) {
            Double pref = userPreference.get(tag);
            if (pref != null && pref > 0) {
                totalScore += pref;
                matchCount++;
            }
        }

        if (matchCount == 0) {
            return 0.3; // 无匹配时给低分
        }

        return Math.min(1.0, (totalScore / matchCount) / 10.0);
    }

    /**
     * 计算热度得分
     */
    private double calculateHotScore(Spot spot, Double avgScore, double maxViewNum, double maxFavourNum) {
        double viewScore = ObjectUtils.isEmpty(spot.getViewNum()) ? 0 : spot.getViewNum() / maxViewNum;
        double favourScore = ObjectUtils.isEmpty(spot.getFavourNum()) ? 0 : spot.getFavourNum() / maxFavourNum;
        double scoreScore = avgScore == null ? 0 : avgScore / 5.0;

        return viewScore * 0.4 + favourScore * 0.3 + scoreScore * 0.3;
    }

    /**
     * 计算预算匹配得分
     */
    private double calculateBudgetScore(Long spotId, BigDecimal budget) {
        if (budget == null || budget.compareTo(BigDecimal.ZERO) <= 0) {
            return 1.0; // 无预算限制时给满分
        }

        BigDecimal minPrice = minPriceCache.get(spotId);
        if (minPrice == null || minPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.8; // 免费景点给高分
        }

        // 单景点预算 = 总预算 / (天数 * 2)
        BigDecimal spotBudget = budget.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);

        if (minPrice.compareTo(spotBudget) <= 0) {
            return 1.0;
        } else if (minPrice.compareTo(spotBudget.multiply(BigDecimal.valueOf(1.5))) <= 0) {
            return 0.6;
        } else {
            return 0.2;
        }
    }

    /**
     * 计算人群适配得分
     */
    private double calculateCrowdScore(List<String> spotTags, String crowdType) {
        if (StringUtils.isBlank(crowdType)) {
            return 1.0;
        }

        Set<String> tagSet = new HashSet<>(spotTags);
        double score = 0.5;

        switch (crowdType.toLowerCase()) {
            case "family":
            case "亲子":
                if (containsAnyTag(spotTags, "亲子", "儿童", "家庭", "乐园")) score = 1.0;
                else if (containsAnyTag(spotTags, "博物馆", "公园", "自然")) score = 0.8;
                else if (containsAnyTag(spotTags, "山", "古迹", "历史")) score = 0.4;
                break;
            case "elder":
            case "长辈":
                if (containsAnyTag(spotTags, "轻松", "休闲", "公园", "园林", "湖")) score = 1.0;
                else if (containsAnyTag(spotTags, "博物馆", "古迹", "寺庙")) score = 0.7;
                else if (containsAnyTag(spotTags, "山", "徒步", "探险")) score = 0.3;
                break;
            case "couple":
            case "情侣":
                if (containsAnyTag(spotTags, "浪漫", "夜景", "海", "日出", "日落")) score = 1.0;
                else if (containsAnyTag(spotTags, "古镇", "园林", "景观")) score = 0.8;
                break;
            case "business":
            case "商务":
                if (containsAnyTag(spotTags, "地标", "现代", "都市", "CBD")) score = 1.0;
                break;
            default:
                score = 0.7;
        }

        return score;
    }

    /**
     * 归一化CF得分
     */
    private double normalizeCfScore(double cfScore) {
        if (cfScore <= 0) return 0;
        // CF得分通常是累加的，需要归一化
        return Math.min(1.0, cfScore / 10.0);
    }

    /**
     * 构建最低价格缓存
     */
    private Map<Long, BigDecimal> buildMinPriceCache() {
        Map<Long, BigDecimal> cache = new HashMap<>();
        List<SpotFee> fees = spotFeeMapper.selectList(
                new QueryWrapper<SpotFee>()
                        .eq("spotFeeStatus", 1)
                        .eq("isDelete", 0)
        );

        for (SpotFee fee : fees) {
            if (fee.getSpotId() != null && fee.getSpotFeePrice() != null) {
                cache.merge(fee.getSpotId(), fee.getSpotFeePrice(), BigDecimal::min);
            }
        }
        return cache;
    }

    /**
     * 构建平均评分缓存
     */
    private Map<Long, Double> buildAvgScoreCache() {
        List<SpotScore> scores = spotScoreMapper.selectList(
                new QueryWrapper<SpotScore>().eq("isDelete", 0)
        );

        return scores.stream()
                .filter(s -> s.getSpotId() != null && s.getScore() != null)
                .collect(Collectors.groupingBy(
                        SpotScore::getSpotId,
                        Collectors.averagingInt(SpotScore::getScore)
                ));
    }

    /**
     * 解析景点标签
     */
    private List<String> parseSpotTags(String tagsJson) {
        if (StringUtils.isBlank(tagsJson)) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.toList(tagsJson, String.class);
        } catch (Exception e) {
            log.warn("解析景点标签失败: {}", tagsJson);
            return Collections.emptyList();
        }
    }

    /**
     * 构建空结果
     */
    private SpotRecommendVO buildEmptyResult(int size, String message) {
        SpotRecommendVO result = new SpotRecommendVO();
        result.setSpotList(Collections.emptyList());
        result.setTotalCount(0);
        result.setAlgorithmInfo(message);
        result.setStrategyType("empty");
        return result;
    }

    /**
     * 构建推荐结果
     */
    private SpotRecommendVO buildRecommendResult(List<RecommendCandidate> candidates, SpotRecommendRequest request) {
        SpotRecommendVO result = new SpotRecommendVO();
        result.setTotalCount(candidates.size());

        List<SpotRecommendVO.RecommendedSpot> spotList = candidates.stream()
                .map(c -> {
                    SpotRecommendVO.RecommendedSpot rs = new SpotRecommendVO.RecommendedSpot();
                    rs.setSpotId(c.getSpot().getId());
                    rs.setSpotName(c.getSpot().getSpotName());
                    rs.setTotalScore(c.getTotalScore());
                    rs.setCfScore(c.getCfScore());
                    rs.setContentScore(c.getTagScore());
                    rs.setHotScore(c.getHotScore());
                    rs.setReason(c.getReason());

                    // 判断推荐来源
                    if (c.getCfScore() > 0 && c.getTagScore() > 0.5) {
                        rs.setSource("hybrid");
                    } else if (c.getCfScore() > 0) {
                        rs.setSource("cf");
                    } else if (c.getTagScore() > 0.5) {
                        rs.setSource("tag");
                    } else {
                        rs.setSource("hot");
                    }

                    return rs;
                })
                .collect(Collectors.toList());

        result.setSpotList(spotList);
        result.setStrategyType("hybrid_cf_content_hot");
        result.setAlgorithmInfo("基于协同过滤与内容特征融合的混合推荐算法，融合CF得分、标签匹配度、热度指数等特征");

        return result;
    }

    // ==================== 内部类：推荐候选景点 ====================
    private static class RecommendCandidate {
        private Spot spot;
        private double totalScore;
        private double cfScore;
        private double tagScore;
        private double hotScore;
        private String reason;
        private boolean excluded;

        public RecommendCandidate(Spot spot, double totalScore, double cfScore,
                                   double tagScore, double hotScore, String reason) {
            this.spot = spot;
            this.totalScore = totalScore;
            this.cfScore = cfScore;
            this.tagScore = tagScore;
            this.hotScore = hotScore;
            this.reason = reason;
            this.excluded = false;
        }

        public Spot getSpot() { return spot; }
        public double getTotalScore() { return totalScore; }
        public double getCfScore() { return cfScore; }
        public double getTagScore() { return tagScore; }
        public double getHotScore() { return hotScore; }
        public String getReason() { return reason; }
        public boolean isExcluded() { return excluded; }
        public void setExcluded(boolean excluded) { this.excluded = excluded; }
    }
}
