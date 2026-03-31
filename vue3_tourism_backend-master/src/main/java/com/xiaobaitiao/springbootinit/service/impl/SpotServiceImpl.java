package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotMapper;
import com.xiaobaitiao.springbootinit.mapper.UserSpotFavoritesMapper;
import com.xiaobaitiao.springbootinit.model.dto.spot.SpotQueryRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.entity.UserSpotFavorites;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.ItemCFRecommendService;
import com.xiaobaitiao.springbootinit.service.SpotScoreService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 景点表服务实现
 *
 * @author toxi
 * 
 */
@Service
@Slf4j
public class SpotServiceImpl extends ServiceImpl<SpotMapper, Spot> implements SpotService {
    @Resource
    private SpotMapper spotMapper;

    @Resource
    private UserService userService;

    @Resource
    private SpotScoreService spotScoreService;

    @Resource
    private UserSpotFavoritesMapper userSpotFavoritesMapper;

    @Resource
    private ItemCFRecommendService itemCFRecommendService;

    /**
     * 混合推荐权重配置
     */
    private static final double ITEMCF_WEIGHT = 0.35;    // ItemCF协同过滤权重
    private static final double TAG_WEIGHT = 0.35;       // 标签偏好权重
    private static final double HOT_WEIGHT = 0.30;        // 热度权重

    /**
     * 校验数据
     *
     * @param spot
     * @param add  对创建的数据进行校验
     */
    @Override
    public void validSpot(Spot spot, boolean add) {
        ThrowUtils.throwIf(spot == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String spotName = spot.getSpotName();
        String spotAvatar = spot.getSpotAvatar();
        String spotLocation = spot.getSpotLocation();
        BigDecimal latitude = spot.getLatitude();
        BigDecimal longitude = spot.getLongitude();
        Integer visitDurationMinutes = spot.getVisitDurationMinutes();
        String openTime = spot.getOpenTime();
        String closeTime = spot.getCloseTime();
        // 创建数据时，参数不能为空
        if (add) {
            // todo 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(spotName), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(spotAvatar), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isBlank(spotLocation), ErrorCode.PARAMS_ERROR);
        }
        if (latitude != null) {
            ThrowUtils.throwIf(latitude.compareTo(BigDecimal.valueOf(-90)) < 0
                    || latitude.compareTo(BigDecimal.valueOf(90)) > 0, ErrorCode.PARAMS_ERROR, "纬度范围应在 -90 到 90 之间");
        }
        if (longitude != null) {
            ThrowUtils.throwIf(longitude.compareTo(BigDecimal.valueOf(-180)) < 0
                    || longitude.compareTo(BigDecimal.valueOf(180)) > 0, ErrorCode.PARAMS_ERROR, "经度范围应在 -180 到 180 之间");
        }
        ThrowUtils.throwIf(latitude == null ^ longitude == null, ErrorCode.PARAMS_ERROR, "经纬度需要同时填写");
        if (visitDurationMinutes != null) {
            ThrowUtils.throwIf(visitDurationMinutes <= 0 || visitDurationMinutes > 24 * 60,
                    ErrorCode.PARAMS_ERROR, "游玩时长需在 1 到 1440 分钟之间");
        }
        if (StringUtils.isNotBlank(openTime)) {
            ThrowUtils.throwIf(!openTime.matches("^([01]\\d|2[0-3]):[0-5]\\d$"),
                    ErrorCode.PARAMS_ERROR, "开放时间格式应为 HH:mm");
        }
        if (StringUtils.isNotBlank(closeTime)) {
            ThrowUtils.throwIf(!closeTime.matches("^([01]\\d|2[0-3]):[0-5]\\d$"),
                    ErrorCode.PARAMS_ERROR, "关闭时间格式应为 HH:mm");
        }
    }

    /**
     * 获取查询条件
     *
     * @param spotQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Spot> getQueryWrapper(SpotQueryRequest spotQueryRequest) {
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        if (spotQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotQueryRequest.getId();
        Long adminId = spotQueryRequest.getAdminId();
        String spotName = spotQueryRequest.getSpotName();
        String spotLocation = spotQueryRequest.getSpotLocation();
        List<String> spotTagList = spotQueryRequest.getSpotTagList();
        String sortField = spotQueryRequest.getSortField();
        String sortOrder = spotQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(spotName), "spotName", spotName);
        queryWrapper.like(StringUtils.isNotBlank(spotLocation), "spotLocation", spotLocation);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(spotTagList)) {
            for (String tag : spotTagList) {
                queryWrapper.like("spotTags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(adminId), "adminId", adminId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取景点表封装
     *
     * @param spot
     * @param request
     * @return
     */
    @Override
    public SpotVO getSpotVO(Spot spot, HttpServletRequest request) {
        // 对象转封装类
        SpotVO spotVO = SpotVO.objToVo(spot);
        return spotVO;
    }

    /**
     * 分页获取景点表封装
     *
     * @param spotPage
     * @param request
     * @return
     */
    @Override
    public Page<SpotVO> getSpotVOPage(Page<Spot> spotPage, HttpServletRequest request) {
        List<Spot> spotList = spotPage.getRecords();
        Page<SpotVO> spotVOPage = new Page<>(spotPage.getCurrent(), spotPage.getSize(), spotPage.getTotal());
        if (CollUtil.isEmpty(spotList)) {
            return spotVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpotVO> spotVOList = spotList.stream().map(SpotVO::objToVo).collect(Collectors.toList());
        spotVOPage.setRecords(spotVOList);
        return spotVOPage;
    }


    @Override
    public List<Spot> getTop10SpotsByViews() {
        return spotMapper.selectTop10SpotsByViews();
    }

    @Override
    public List<SpotVO> getRecommendSpotVOList(Integer size, HttpServletRequest request) {
        int recommendSize = normalizeRecommendSize(size);
        List<Spot> openSpotList = this.list(new QueryWrapper<Spot>()
                .eq("spotStatus", 1)
                .orderByDesc("viewNum")
                .orderByDesc("favourNum"));
        if (CollUtil.isEmpty(openSpotList)) {
            return Collections.emptyList();
        }

        Map<Long, Spot> openSpotMap = openSpotList.stream()
                .collect(Collectors.toMap(Spot::getId, s -> s, (a, b) -> a));

        List<SpotScore> allSpotScores = spotScoreService.list(new QueryWrapper<SpotScore>()
                .select("spotId", "userId", "score"));
        Map<Long, Double> avgScoreMap = buildAverageScoreMap(allSpotScores);

        double maxViewNum = openSpotList.stream()
                .map(Spot::getViewNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);
        double maxFavourNum = openSpotList.stream()
                .map(Spot::getFavourNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);

        User loginUser = userService.getLoginUserPermitNull(request);

        // 未登录用户，返回热门推荐
        if (loginUser == null) {
            return buildHotSpotVOList(openSpotList, avgScoreMap, recommendSize, "热门景点推荐");
        }

        Long userId = loginUser.getId();

        // ========== 1. 基于用户历史行为构建标签偏好 ==========
        List<UserSpotFavorites> favoriteList = userSpotFavoritesMapper.selectList(new QueryWrapper<UserSpotFavorites>()
                .eq("userId", userId)
                .eq("status", 1));
        List<SpotScore> userScoreList = allSpotScores.stream()
                .filter(spotScore -> userId.equals(spotScore.getUserId()))
                .collect(Collectors.toList());

        Map<String, Double> userTagPreferenceMap = buildUserTagPreferenceMap(openSpotList, favoriteList, userScoreList);
        Set<Long> interactedSpotIdSet = buildInteractedSpotIdSet(favoriteList, userScoreList);

        // 无历史行为，返回热门推荐
        if (userTagPreferenceMap.isEmpty()) {
            return buildHotSpotVOList(openSpotList, avgScoreMap, recommendSize, "根据热门程度为你推荐");
        }

        // ========== 2. 获取 ItemCF 协同过滤推荐结果 ==========
        List<Long> itemCFRecommendSpotIds = itemCFRecommendService.getItemCFRecommendations(userId, recommendSize * 2);
        Map<Long, Double> itemCFScoreMap = new HashMap<>();
        for (int i = 0; i < itemCFRecommendSpotIds.size(); i++) {
            // 得分从高到低递减
            itemCFScoreMap.put(itemCFRecommendSpotIds.get(i), (double) (itemCFRecommendSpotIds.size() - i));
        }

        // ========== 3. 混合评分计算 ==========
        List<RecommendSpotCandidate> candidateList = new ArrayList<>();
        for (Spot spot : openSpotList) {
            if (interactedSpotIdSet.contains(spot.getId())) {
                continue;
            }
            Long spotId = spot.getId();

            // 标签偏好得分
            List<String> tagList = parseSpotTags(spot.getSpotTags());
            double tagScore = calculatePersonalizationScore(tagList, userTagPreferenceMap);

            // 热度得分
            double hotScore = calculateHotScore(spot, avgScoreMap.getOrDefault(spotId, 0D), maxViewNum, maxFavourNum);

            // ItemCF协同过滤得分（归一化到0-1）
            double itemCFMaxScore = itemCFRecommendSpotIds.size();
            double itemCFScore = (itemCFScoreMap.containsKey(spotId) && itemCFMaxScore > 0)
                    ? itemCFScoreMap.get(spotId) / itemCFMaxScore
                    : 0.0;

            // 混合总分
            double finalScore = itemCFScore * ITEMCF_WEIGHT + tagScore * TAG_WEIGHT + hotScore * HOT_WEIGHT;

            // 构建推荐理由
            String recommendReason = buildRecommendReason(spot, tagList, userTagPreferenceMap,
                    avgScoreMap.getOrDefault(spotId, 0D), itemCFScoreMap.containsKey(spotId));

            candidateList.add(new RecommendSpotCandidate(spot, finalScore, recommendReason));
        }

        if (candidateList.isEmpty()) {
            return buildHotSpotVOList(openSpotList, avgScoreMap, recommendSize, "根据热门程度为你推荐");
        }

        return candidateList.stream()
                .sorted(Comparator.comparingDouble(RecommendSpotCandidate::getScore).reversed())
                .limit(recommendSize)
                .map(candidate -> {
                    SpotVO spotVO = SpotVO.objToVo(candidate.getSpot());
                    spotVO.setRecommendReason(candidate.getRecommendReason());
                    return spotVO;
                })
                .collect(Collectors.toList());
    }

    private int normalizeRecommendSize(Integer size) {
        if (size == null || size <= 0) {
            return 10;
        }
        return Math.min(size, 20);
    }

    private Map<Long, Double> buildAverageScoreMap(List<SpotScore> spotScoreList) {
        if (CollUtil.isEmpty(spotScoreList)) {
            return Collections.emptyMap();
        }
        return spotScoreList.stream()
                .filter(spotScore -> spotScore.getSpotId() != null && spotScore.getScore() != null)
                .collect(Collectors.groupingBy(SpotScore::getSpotId,
                        Collectors.averagingInt(SpotScore::getScore)));
    }

    private Map<String, Double> buildUserTagPreferenceMap(List<Spot> openSpotList,
                                                          List<UserSpotFavorites> favoriteList,
                                                          List<SpotScore> userScoreList) {
        Map<Long, Spot> spotMap = openSpotList.stream()
                .collect(Collectors.toMap(Spot::getId, spot -> spot, (left, right) -> left));
        Map<String, Double> tagPreferenceMap = new HashMap<>();

        for (UserSpotFavorites favorite : favoriteList) {
            Spot spot = spotMap.get(favorite.getSpotId());
            if (spot == null) {
                continue;
            }
            for (String tag : parseSpotTags(spot.getSpotTags())) {
                tagPreferenceMap.merge(tag, 3D, Double::sum);
            }
        }

        for (SpotScore spotScore : userScoreList) {
            Spot spot = spotMap.get(spotScore.getSpotId());
            if (spot == null || spotScore.getScore() == null) {
                continue;
            }
            double ratingWeight = (spotScore.getScore() - 3) * 1.5;
            if (ratingWeight == 0) {
                continue;
            }
            for (String tag : parseSpotTags(spot.getSpotTags())) {
                tagPreferenceMap.merge(tag, ratingWeight, Double::sum);
            }
        }
        return tagPreferenceMap;
    }

    private Set<Long> buildInteractedSpotIdSet(List<UserSpotFavorites> favoriteList, List<SpotScore> userScoreList) {
        Set<Long> interactedSpotIdSet = new HashSet<>();
        favoriteList.stream()
                .map(UserSpotFavorites::getSpotId)
                .filter(ObjectUtils::isNotEmpty)
                .forEach(interactedSpotIdSet::add);
        userScoreList.stream()
                .map(SpotScore::getSpotId)
                .filter(ObjectUtils::isNotEmpty)
                .forEach(interactedSpotIdSet::add);
        return interactedSpotIdSet;
    }

    private List<SpotVO> buildHotSpotVOList(List<Spot> openSpotList,
                                            Map<Long, Double> avgScoreMap,
                                            int recommendSize,
                                            String defaultReason) {
        double maxViewNum = openSpotList.stream()
                .map(Spot::getViewNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);
        double maxFavourNum = openSpotList.stream()
                .map(Spot::getFavourNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);

        return openSpotList.stream()
                .sorted((left, right) -> Double.compare(
                        calculateHotScore(right, avgScoreMap.getOrDefault(right.getId(), 0D), maxViewNum, maxFavourNum),
                        calculateHotScore(left, avgScoreMap.getOrDefault(left.getId(), 0D), maxViewNum, maxFavourNum)))
                .limit(recommendSize)
                .map(spot -> {
                    SpotVO spotVO = SpotVO.objToVo(spot);
                    spotVO.setRecommendReason(buildRecommendReason(parseSpotTags(spot.getSpotTags()),
                            Collections.emptyMap(), avgScoreMap.getOrDefault(spot.getId(), 0D), spot, false));
                    if (StringUtils.isBlank(spotVO.getRecommendReason())) {
                        spotVO.setRecommendReason(defaultReason);
                    }
                    return spotVO;
                })
                .collect(Collectors.toList());
    }

    private double calculateHotScore(Spot spot, Double avgScore, double maxViewNum, double maxFavourNum) {
        double normalizedViewScore = ObjectUtils.isEmpty(spot.getViewNum()) ? 0D : spot.getViewNum() / maxViewNum;
        double normalizedFavourScore = ObjectUtils.isEmpty(spot.getFavourNum()) ? 0D : spot.getFavourNum() / maxFavourNum;
        double normalizedAvgScore = avgScore == null ? 0D : avgScore / 5D;
        return normalizedViewScore * 0.45 + normalizedFavourScore * 0.30 + normalizedAvgScore * 0.25;
    }

    private double calculatePersonalizationScore(List<String> tagList, Map<String, Double> userTagPreferenceMap) {
        if (CollUtil.isEmpty(tagList) || userTagPreferenceMap.isEmpty()) {
            return 0D;
        }
        double totalPreferenceScore = 0D;
        for (String tag : tagList) {
            totalPreferenceScore += userTagPreferenceMap.getOrDefault(tag, 0D);
        }
        double normalizedScore = totalPreferenceScore / tagList.size();
        return Math.max(normalizedScore, 0D);
    }

    private String buildRecommendReason(Spot spot,
                                        List<String> tagList,
                                        Map<String, Double> userTagPreferenceMap,
                                        Double avgScore,
                                        boolean hitByItemCF) {
        StringBuilder reason = new StringBuilder();

        // 优先显示协同过滤推荐理由
        if (hitByItemCF) {
            reason.append("相似用户也在逛 ");
        }

        // 标签匹配
        List<String> matchedTagList = tagList.stream()
                .filter(tag -> userTagPreferenceMap.getOrDefault(tag, 0D) > 0)
                .limit(2)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(matchedTagList)) {
            if (reason.length() > 0) {
                reason.append("· ");
            }
            reason.append("匹配你的偏好：").append(String.join("、", matchedTagList));
        }

        // 评分高
        if (avgScore != null && avgScore >= 4.5) {
            if (reason.length() > 0) {
                reason.append("· ");
            }
            reason.append("评分").append(String.format("%.1f", avgScore)).append("分");
        }

        // 收藏多
        if (ObjectUtils.isNotEmpty(spot.getFavourNum()) && spot.getFavourNum() >= 10) {
            if (reason.length() > 0) {
                reason.append("· ");
            }
            reason.append("收藏").append(spot.getFavourNum()).append("次");
        }

        return reason.length() > 0 ? reason.toString() : "综合推荐";
    }

    private List<String> parseSpotTags(String spotTags) {
        if (StringUtils.isBlank(spotTags)) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.toList(spotTags, String.class);
        } catch (Exception e) {
            log.warn("parse spot tags error, spotTags={}", spotTags, e);
            return Collections.emptyList();
        }
    }

    private static class RecommendSpotCandidate {
        private final Spot spot;
        private final double score;
        private final String recommendReason;

        private RecommendSpotCandidate(Spot spot, double score, String recommendReason) {
            this.spot = spot;
            this.score = score;
            this.recommendReason = recommendReason;
        }

        public Spot getSpot() {
            return spot;
        }

        public double getScore() {
            return score;
        }

        public String getRecommendReason() {
            return recommendReason;
        }
    }

}
