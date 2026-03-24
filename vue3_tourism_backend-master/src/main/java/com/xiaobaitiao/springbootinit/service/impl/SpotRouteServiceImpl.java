package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.manager.TourismAiClient;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotFeeMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotRouteMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteAdjustRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRoutePlanRequest;
import com.xiaobaitiao.springbootinit.model.vo.RouteAdjustmentAnalysisVO;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanDayVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanTimeBlockVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotRouteVO;
import com.xiaobaitiao.springbootinit.service.SpotRouteService;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.utils.PositionUtil;
import com.xiaobaitiao.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 景点路线表服务实现
 *
 * @author toxi
 * @from <a href="https://luoye6.github.io/"> 个人博客
 */
@Service
@Slf4j
public class SpotRouteServiceImpl extends ServiceImpl<SpotRouteMapper, SpotRoute> implements SpotRouteService {

    private static final String PACE_RELAXED = "RELAXED";

    private static final String PACE_NORMAL = "NORMAL";

    private static final String PACE_COMPACT = "COMPACT";

    private static final String ADJUST_RELAXED = "RELAXED";

    private static final String ADJUST_SAVE_BUDGET = "SAVE_BUDGET";

    private static final String ADJUST_ADD_NIGHT_VIEW = "ADD_NIGHT_VIEW";

    private static final int DEFAULT_VISIT_DURATION_MINUTES = 120;

    private static final int DEFAULT_TRANSIT_MINUTES = 30;

    private static final int MAX_DAILY_ACTIVITY_MINUTES = 9 * 60;

    private static final int DAY_START_MINUTES = 9 * 60;

    private static final int MORNING_END_MINUTES = 12 * 60;

    private static final int AFTERNOON_END_MINUTES = 18 * 60;

    private static final String BLOCK_MORNING = "MORNING";

    private static final String BLOCK_AFTERNOON = "AFTERNOON";

    private static final String BLOCK_EVENING = "EVENING";

    private static final int AFTERNOON_START_MINUTES = 13 * 60;

    private static final int EVENING_START_MINUTES = 18 * 60 + 30;


    @Resource
    private SpotService spotService;

    @Resource
    private SpotFeeMapper spotFeeMapper;

    @Resource
    private SpotScoreMapper spotScoreMapper;

    @Resource
    private TourismAiClient tourismAiClient;

    /**
     * 校验数据
     *
     * @param spotRoute
     * @param add       对创建的数据进行校验
     */
    @Override
    public void validSpotRoute(SpotRoute spotRoute, boolean add) {
        ThrowUtils.throwIf(spotRoute == null, ErrorCode.PARAMS_ERROR);
        // todo 从对象中取值
        String spotIds = spotRoute.getSpotIds();
        String spotRouteAvatar = spotRoute.getSpotRouteAvatar();
        String spotRouteDescription = spotRoute.getSpotRouteDescription();
        // 创建数据时，参数不能为空
        ThrowUtils.throwIf(StringUtils.isBlank(spotIds), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StringUtils.isBlank(spotRouteAvatar), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StringUtils.isBlank(spotRouteDescription), ErrorCode.PARAMS_ERROR);
    }

    /**
     * 获取查询条件
     *
     * @param spotRouteQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<SpotRoute> getQueryWrapper(SpotRouteQueryRequest spotRouteQueryRequest) {
        QueryWrapper<SpotRoute> queryWrapper = new QueryWrapper<>();
        if (spotRouteQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = spotRouteQueryRequest.getId();
        Long adminId = spotRouteQueryRequest.getAdminId();
        List<String> spotIdList = spotRouteQueryRequest.getSpotIdList();
        String spotRouteDescription = spotRouteQueryRequest.getSpotRouteDescription();
        String sortField = spotRouteQueryRequest.getSortField();
        String sortOrder = spotRouteQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(spotRouteDescription), "spotRouteDescription", spotRouteDescription);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(spotIdList)) {
            for (String tag : spotIdList) {
                queryWrapper.like("spotIds", "\"" + tag + "\"");
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
     * 获取景点路线表封装
     *
     * @param spotRoute 景点路线实体
     * @param request   HTTP请求
     * @return 返回封装后的 SpotRouteVO
     */
    @Override
    public SpotRouteVO getSpotRouteVO(SpotRoute spotRoute, HttpServletRequest request) {
        // 对象转封装类
        SpotRouteVO spotRouteVO = SpotRouteVO.objToVo(spotRoute);

        // 获取景点ID列表
        List<String> spotIdList = spotRouteVO.getSpotIdList();
        if (CollUtil.isNotEmpty(spotIdList)) {
            // 查询景点名称列表
            List<String> spotNameList = spotIdList.stream()
                    .map(spotId -> {
                        Spot spot = spotService.getById(spotId);
                        return spot != null ? spot.getSpotName() : "未知景点";
                    })
                    .collect(Collectors.toList());
            spotRouteVO.setSpotNameList(spotNameList);

            // 计算景点距离列表
            List<Double> spotDistanceList = new ArrayList<>();
            for (int i = 0; i < spotIdList.size() - 1; i++) {
                String currentSpotId = spotIdList.get(i);
                String nextSpotId = spotIdList.get(i + 1);

                Spot currentSpot = spotService.getById(currentSpotId);
                Spot nextSpot = spotService.getById(nextSpotId);

                if (currentSpot != null && nextSpot != null) {
                    double distance = calculateDistance(currentSpot, nextSpot);
                    spotDistanceList.add(distance >= Double.MAX_VALUE / 4 ? 0.0 : distance);
                } else {
                    spotDistanceList.add(0.0); // 景点不存在时，默认距离为0
                }
            }
            spotRouteVO.setSpotDistanceList(spotDistanceList);
        }

        return spotRouteVO;
    }
    /**
     * 分页获取景点路线表封装
     *
     * @param spotRoutePage
     * @param request
     * @return
     */
    @Override
    public Page<SpotRouteVO> getSpotRouteVOPage(Page<SpotRoute> spotRoutePage, HttpServletRequest request) {
        List<SpotRoute> spotRouteList = spotRoutePage.getRecords();
        Page<SpotRouteVO> spotRouteVOPage = new Page<>(spotRoutePage.getCurrent(), spotRoutePage.getSize(), spotRoutePage.getTotal());
        if (CollUtil.isEmpty(spotRouteList)) {
            return spotRouteVOPage;
        }

        // 对象列表 => 封装对象列表
        List<SpotRouteVO> spotRouteVOList = spotRouteList.stream().map(spotRoute -> {
            SpotRouteVO spotRouteVO = SpotRouteVO.objToVo(spotRoute);

            // 获取景点ID列表
            List<String> spotIdList = spotRouteVO.getSpotIdList();
            if (CollUtil.isNotEmpty(spotIdList)) {
                // 查询景点名称列表
                List<String> spotNameList = spotIdList.stream()
                        .map(spotId -> {
                            Spot spot = spotService.getById(spotId);
                            return spot != null ? spot.getSpotName() : "未知景点";
                        })
                        .collect(Collectors.toList());
                spotRouteVO.setSpotNameList(spotNameList);

                // 计算景点距离列表
                List<Double> spotDistanceList = new ArrayList<>();
                for (int i = 0; i < spotIdList.size() - 1; i++) {
                    String currentSpotId = spotIdList.get(i);
                    String nextSpotId = spotIdList.get(i + 1);

                    Spot currentSpot = spotService.getById(currentSpotId);
                    Spot nextSpot = spotService.getById(nextSpotId);

                    if (currentSpot != null && nextSpot != null) {
                        double distance = calculateDistance(currentSpot, nextSpot);
                        spotDistanceList.add(distance >= Double.MAX_VALUE / 4 ? 0.0 : distance);
                    } else {
                        spotDistanceList.add(0.0); // 景点不存在时，默认距离为0
                    }
                }
                spotRouteVO.setSpotDistanceList(spotDistanceList);
            }

            return spotRouteVO;
        }).collect(Collectors.toList());

        spotRouteVOPage.setRecords(spotRouteVOList);
        return spotRouteVOPage;
    }

    @Override
    public SpotRoutePlanVO generateRoutePlan(SpotRoutePlanRequest spotRoutePlanRequest, HttpServletRequest request) {
        validatePlanRequest(spotRoutePlanRequest);
        int dayCount = spotRoutePlanRequest.getDayCount();
        BigDecimal budget = spotRoutePlanRequest.getBudget();
        String paceType = normalizePaceType(spotRoutePlanRequest.getPaceType());
        String targetLocation = StringUtils.trimToEmpty(spotRoutePlanRequest.getSpotLocation());
        List<String> preferredTagList = CollUtil.emptyIfNull(spotRoutePlanRequest.getSpotTagList()).stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
        List<String> excludeTagList = CollUtil.emptyIfNull(spotRoutePlanRequest.getExcludeTagList()).stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
        List<Long> candidateSpotIdList = CollUtil.emptyIfNull(spotRoutePlanRequest.getCandidateSpotIdList()).stream()
                .filter(ObjectUtils::isNotEmpty)
                .distinct()
                .collect(Collectors.toList());

        List<Spot> openSpotList = spotService.list(new QueryWrapper<Spot>()
                .eq("spotStatus", 1)
                .orderByDesc("viewNum")
                .orderByDesc("favourNum"));
        ThrowUtils.throwIf(CollUtil.isEmpty(openSpotList), ErrorCode.NOT_FOUND_ERROR, "暂无可规划景点");

        if (CollUtil.isNotEmpty(candidateSpotIdList)) {
            Set<Long> candidateSpotIdSet = new LinkedHashSet<>(candidateSpotIdList);
            openSpotList = openSpotList.stream()
                    .filter(spot -> candidateSpotIdSet.contains(spot.getId()))
                    .collect(Collectors.toList());
            ThrowUtils.throwIf(CollUtil.isEmpty(openSpotList), ErrorCode.NOT_FOUND_ERROR, "候选景点不足，无法规划路线");
        }

        List<Spot> candidateSpotList = openSpotList.stream()
                .filter(spot -> matchLocation(spot, targetLocation))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(candidateSpotList)) {
            candidateSpotList = openSpotList;
        }
        if (CollUtil.isNotEmpty(excludeTagList)) {
            List<Spot> filteredSpotList = candidateSpotList.stream()
                    .filter(spot -> !matchesExcludedTags(spot, excludeTagList))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(filteredSpotList)) {
                candidateSpotList = filteredSpotList;
            }
        }

        Map<Long, BigDecimal> minPriceMap = buildMinPriceMap();
        Map<Long, Double> avgScoreMap = buildAverageScoreMap();
        double maxViewNum = candidateSpotList.stream()
                .map(Spot::getViewNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);
        double maxFavourNum = candidateSpotList.stream()
                .map(Spot::getFavourNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);

        List<RouteSpotCandidate> routeSpotCandidateList = candidateSpotList.stream()
                .map(spot -> buildRouteSpotCandidate(spot, preferredTagList, minPriceMap, avgScoreMap,
                        maxViewNum, maxFavourNum, budget, dayCount, paceType))
                .sorted(Comparator.comparingDouble(RouteSpotCandidate::getScore).reversed())
                .collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(routeSpotCandidateList), ErrorCode.NOT_FOUND_ERROR, "暂无符合条件的景点");

        List<RouteSpotCandidate> selectedCandidateList = selectRouteCandidates(routeSpotCandidateList, budget, dayCount, paceType);
        List<RouteSpotCandidate> orderedCandidateList = sortCandidatesByDistance(selectedCandidateList);

        SpotRoutePlanVO planVO = new SpotRoutePlanVO();
        List<SpotVO> orderedSpotVOList = orderedCandidateList.stream()
                .map(candidate -> {
                    SpotVO spotVO = SpotVO.objToVo(candidate.getSpot());
                    spotVO.setRecommendReason(candidate.getReason());
                    return spotVO;
                })
                .collect(Collectors.toList());
        List<String> orderedSpotIdList = orderedCandidateList.stream()
                .map(candidate -> String.valueOf(candidate.getSpot().getId()))
                .collect(Collectors.toList());
        List<String> orderedSpotNameList = orderedCandidateList.stream()
                .map(candidate -> candidate.getSpot().getSpotName())
                .collect(Collectors.toList());
        List<Double> spotDistanceList = buildDistanceList(orderedCandidateList);

        planVO.setRouteTitle(dayCount + "天智能旅游路线");
        planVO.setRouteDescription(buildRouteDescription(targetLocation, preferredTagList, budget, orderedSpotNameList, paceType));
        planVO.setTotalDays(dayCount);
        planVO.setTotalSpotCount(orderedCandidateList.size());
        planVO.setTotalEstimatedCost(sumCandidateCost(orderedCandidateList));
        planVO.setCoverSpotAvatar(orderedCandidateList.get(0).getSpot().getSpotAvatar());
        planVO.setSpotIdList(orderedSpotIdList);
        planVO.setSpotNameList(orderedSpotNameList);
        planVO.setSpotDistanceList(spotDistanceList);
        planVO.setMatchedTagList(buildMatchedTagList(orderedCandidateList, preferredTagList));
        planVO.setSpotList(orderedSpotVOList);
        planVO.setDayPlanList(buildDayPlanList(orderedCandidateList, dayCount, paceType));
        return planVO;
    }

    @Override
    public SpotRoutePlanVO adjustRoutePlan(SpotRouteAdjustRequest spotRouteAdjustRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spotRouteAdjustRequest == null, ErrorCode.PARAMS_ERROR);
        SpotRoutePlanRequest originalRequest = spotRouteAdjustRequest.getOriginalRequest();
        ThrowUtils.throwIf(originalRequest == null, ErrorCode.PARAMS_ERROR, "缺少原始路线请求");

        SpotRoutePlanRequest adjustedRequest = copyPlanRequest(originalRequest);
        String adjustmentSummary;
        String adjustType = StringUtils.upperCase(StringUtils.trimToEmpty(spotRouteAdjustRequest.getAdjustType()));
        if (StringUtils.isNotBlank(adjustType)) {
            adjustmentSummary = applyAdjustment(adjustedRequest, adjustType, spotRouteAdjustRequest.getCurrentEstimatedCost());
        } else {
            String customInstruction = StringUtils.trimToEmpty(spotRouteAdjustRequest.getCustomInstruction());
            ThrowUtils.throwIf(StringUtils.isBlank(customInstruction), ErrorCode.PARAMS_ERROR, "缺少微调类型或自然语言指令");
            RouteAdjustmentIntent adjustmentIntent = parseNaturalLanguageAdjustmentIntent(customInstruction, adjustedRequest, spotRouteAdjustRequest.getCurrentEstimatedCost());
            adjustmentSummary = applyNaturalLanguageAdjustment(adjustedRequest, adjustmentIntent, spotRouteAdjustRequest.getCurrentEstimatedCost());
            SpotRoutePlanVO planVO = generateRoutePlan(adjustedRequest, request);
            planVO.setAdjustmentSummary(adjustmentSummary);
            planVO.setAdjustmentAnalysis(buildAdjustmentAnalysisVO(adjustmentIntent));
            planVO.setRouteTitle(planVO.getRouteTitle() + "（已微调）");
            return planVO;
        }
        SpotRoutePlanVO planVO = generateRoutePlan(adjustedRequest, request);
        planVO.setAdjustmentSummary(adjustmentSummary);
        planVO.setRouteTitle(planVO.getRouteTitle() + "（已微调）");
        return planVO;
    }

    /**
     * 解析经纬度字符串，返回 [纬度, 经度] 数组
     *
     * @param location 经纬度字符串，格式如 "30.1670° N, 118.0500° E"
     * @return 返回 [纬度, 经度] 数组，解析失败返回 null
     */
    private double[] parseLocation(String location) {
        if (StrUtil.isBlank(location)) {
            return null;
        }
        try {
            String[] parts = location.split(",");
            if (parts.length != 2) {
                return null;
            }
            double latitude = parseCoordinate(parts[0].trim());
            double longitude = parseCoordinate(parts[1].trim());
            return new double[]{latitude, longitude};
        } catch (Exception e) {
            return null;
        }
    }

    private double[] extractCoordinates(Spot spot) {
        if (spot == null) {
            return null;
        }
        if (spot.getLatitude() != null && spot.getLongitude() != null) {
            return new double[]{spot.getLatitude().doubleValue(), spot.getLongitude().doubleValue()};
        }
        return parseLocation(spot.getSpotLocation());
    }

    /**
     * 解析单个坐标值，如 "30.1670° N"
     *
     * @param coordinate 坐标字符串
     * @return 返回解析后的数值
     */
    private double parseCoordinate(String coordinate) {
        if (StrUtil.isBlank(coordinate)) {
            throw new IllegalArgumentException("坐标字符串为空");
        }
        String[] parts = coordinate.split("°");
        if (parts.length != 2) {
            throw new IllegalArgumentException("坐标格式错误");
        }
        double value = Double.parseDouble(parts[0].trim());
        String direction = parts[1].trim();
        if ("S".equalsIgnoreCase(direction) || "W".equalsIgnoreCase(direction)) {
            value = -value; // 南纬和西经为负数
        }
        return value;
    }

    private void validatePlanRequest(SpotRoutePlanRequest spotRoutePlanRequest) {
        ThrowUtils.throwIf(spotRoutePlanRequest == null, ErrorCode.PARAMS_ERROR);
        Integer dayCount = spotRoutePlanRequest.getDayCount();
        ThrowUtils.throwIf(dayCount == null || dayCount <= 0 || dayCount > 7, ErrorCode.PARAMS_ERROR, "游玩天数需在 1 到 7 天之间");
        BigDecimal budget = spotRoutePlanRequest.getBudget();
        if (budget != null) {
            ThrowUtils.throwIf(budget.compareTo(BigDecimal.ZERO) <= 0, ErrorCode.PARAMS_ERROR, "预算必须大于 0");
        }
        String paceType = spotRoutePlanRequest.getPaceType();
        if (StringUtils.isNotBlank(paceType)) {
            ThrowUtils.throwIf(!PACE_RELAXED.equalsIgnoreCase(paceType)
                    && !PACE_NORMAL.equalsIgnoreCase(paceType)
                    && !PACE_COMPACT.equalsIgnoreCase(paceType), ErrorCode.PARAMS_ERROR, "不支持的路线节奏类型");
        }
    }

    private boolean matchLocation(Spot spot, String targetLocation) {
        if (StringUtils.isBlank(targetLocation)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(StringUtils.defaultString(spot.getSpotLocation()), targetLocation);
    }

    private boolean matchesExcludedTags(Spot spot, List<String> excludeTagList) {
        List<String> tagList = parseTagList(spot.getSpotTags());
        for (String excludeTag : excludeTagList) {
            for (String tag : tagList) {
                if (StringUtils.equalsIgnoreCase(tag, excludeTag) || StringUtils.containsIgnoreCase(tag, excludeTag)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<Long, BigDecimal> buildMinPriceMap() {
        List<SpotFee> spotFeeList = spotFeeMapper.selectList(new QueryWrapper<SpotFee>()
                .eq("spotFeeStatus", 1));
        if (CollUtil.isEmpty(spotFeeList)) {
            return Collections.emptyMap();
        }
        Map<Long, BigDecimal> minPriceMap = new HashMap<>();
        for (SpotFee spotFee : spotFeeList) {
            if (spotFee.getSpotId() == null || spotFee.getSpotFeePrice() == null) {
                continue;
            }
            minPriceMap.merge(spotFee.getSpotId(), spotFee.getSpotFeePrice(), BigDecimal::min);
        }
        return minPriceMap;
    }

    private Map<Long, Double> buildAverageScoreMap() {
        List<SpotScore> scoreList = spotScoreMapper.selectList(new QueryWrapper<SpotScore>()
                .select("spotId", "score"));
        if (CollUtil.isEmpty(scoreList)) {
            return Collections.emptyMap();
        }
        return scoreList.stream()
                .filter(spotScore -> spotScore.getSpotId() != null && spotScore.getScore() != null)
                .collect(Collectors.groupingBy(SpotScore::getSpotId, Collectors.averagingInt(SpotScore::getScore)));
    }

    private RouteSpotCandidate buildRouteSpotCandidate(Spot spot,
                                                       List<String> preferredTagList,
                                                       Map<Long, BigDecimal> minPriceMap,
                                                       Map<Long, Double> avgScoreMap,
                                                       double maxViewNum,
                                                       double maxFavourNum,
                                                       BigDecimal budget,
                                                       int dayCount,
                                                       String paceType) {
        List<String> tagList = parseTagList(spot.getSpotTags());
        List<String> matchedTagList = tagList.stream()
                .filter(preferredTagList::contains)
                .distinct()
                .collect(Collectors.toList());
        BigDecimal estimatedCost = minPriceMap.getOrDefault(spot.getId(), BigDecimal.ZERO);
        Double avgScore = avgScoreMap.getOrDefault(spot.getId(), 0D);

        double tagScore = matchedTagList.size() * 5D;
        double ratingScore = avgScore * 1.8D;
        double hotScore = ((ObjectUtils.isEmpty(spot.getViewNum()) ? 0D : spot.getViewNum() / maxViewNum) * 2.5D)
                + ((ObjectUtils.isEmpty(spot.getFavourNum()) ? 0D : spot.getFavourNum() / maxFavourNum) * 2D);
        double budgetFriendlyScore = 0D;
        if (budget != null) {
            BigDecimal averageBudgetPerSpot = budget.divide(BigDecimal.valueOf(Math.max(dayCount * 2, 1)), 2, RoundingMode.HALF_UP);
            if (estimatedCost.compareTo(BigDecimal.ZERO) == 0 || estimatedCost.compareTo(averageBudgetPerSpot) <= 0) {
                budgetFriendlyScore = 2D;
            }
        }
        double pacePreferenceScore = buildPacePreferenceScore(tagList, matchedTagList, estimatedCost, budget, paceType, spot);
        double score = tagScore + ratingScore + hotScore + budgetFriendlyScore + pacePreferenceScore;
        return new RouteSpotCandidate(spot, score, estimatedCost, matchedTagList,
                buildSpotPlanReason(matchedTagList, avgScore, estimatedCost, budget),
                resolvePreferredTimeBlock(spot, tagList));
    }

    private List<RouteSpotCandidate> selectRouteCandidates(List<RouteSpotCandidate> candidateList,
                                                           BigDecimal budget,
                                                           int dayCount,
                                                           String paceType) {
        int expectedSpotCount = estimateExpectedSpotCount(candidateList, dayCount, paceType);
        List<RouteSpotCandidate> selectedCandidateList = new ArrayList<>();
        BigDecimal currentCost = BigDecimal.ZERO;

        for (RouteSpotCandidate candidate : candidateList) {
            if (selectedCandidateList.size() >= expectedSpotCount) {
                break;
            }
            if (budget == null) {
                selectedCandidateList.add(candidate);
                continue;
            }
            BigDecimal nextCost = currentCost.add(candidate.getEstimatedCost());
            if (nextCost.compareTo(budget) <= 0 || selectedCandidateList.size() < dayCount) {
                selectedCandidateList.add(candidate);
                currentCost = nextCost;
            }
        }

        if (selectedCandidateList.size() < Math.min(dayCount, candidateList.size())) {
            for (RouteSpotCandidate candidate : candidateList) {
                if (selectedCandidateList.contains(candidate)) {
                    continue;
                }
                selectedCandidateList.add(candidate);
                if (selectedCandidateList.size() >= Math.min(dayCount, candidateList.size())) {
                    break;
                }
            }
        }
        return selectedCandidateList;
    }

    private int estimateExpectedSpotCount(List<RouteSpotCandidate> candidateList, int dayCount, String paceType) {
        if (CollUtil.isEmpty(candidateList)) {
            return dayCount;
        }
        int paceDrivenExpectedCount = (int) Math.round(dayCount * resolveAverageSpotCountPerDay(paceType));
        double averageVisitMinutes = candidateList.stream()
                .limit(Math.min(candidateList.size(), 10))
                .map(RouteSpotCandidate::getSpot)
                .mapToInt(this::getVisitDurationMinutes)
                .average()
                .orElse(DEFAULT_VISIT_DURATION_MINUTES);
        double averageSpotUnitMinutes = averageVisitMinutes + DEFAULT_TRANSIT_MINUTES + resolveBreakMinutesBetweenSpots(paceType);
        int maxDailyActivityMinutes = resolveMaxDailyActivityMinutes(paceType);
        int capacityBasedExpectedCount = (int) Math.floor(dayCount * maxDailyActivityMinutes / averageSpotUnitMinutes);
        int expectedSpotCount = Math.min(capacityBasedExpectedCount, paceDrivenExpectedCount);
        if (PACE_COMPACT.equals(normalizePaceType(paceType))) {
            expectedSpotCount = Math.max(expectedSpotCount, Math.min(candidateList.size(), dayCount * 2));
        }
        return Math.min(Math.max(expectedSpotCount, dayCount), candidateList.size());
    }

    private List<RouteSpotCandidate> sortCandidatesByDistance(List<RouteSpotCandidate> candidateList) {
        if (candidateList.size() <= 2) {
            return candidateList;
        }
        List<RouteSpotCandidate> remainingCandidateList = new ArrayList<>(candidateList);
        List<RouteSpotCandidate> orderedCandidateList = new ArrayList<>();
        RouteSpotCandidate currentCandidate = remainingCandidateList.remove(0);
        orderedCandidateList.add(currentCandidate);

        while (CollUtil.isNotEmpty(remainingCandidateList)) {
            RouteSpotCandidate nearestCandidate = null;
            double nearestDistance = Double.MAX_VALUE;
            for (RouteSpotCandidate candidate : remainingCandidateList) {
                double distance = calculateDistance(currentCandidate.getSpot(), candidate.getSpot());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestCandidate = candidate;
                }
            }
            if (nearestCandidate == null) {
                orderedCandidateList.addAll(remainingCandidateList);
                break;
            }
            orderedCandidateList.add(nearestCandidate);
            remainingCandidateList.remove(nearestCandidate);
            currentCandidate = nearestCandidate;
        }
        return orderedCandidateList;
    }

    private double calculateDistance(Spot leftSpot, Spot rightSpot) {
        double[] leftCoords = extractCoordinates(leftSpot);
        double[] rightCoords = extractCoordinates(rightSpot);
        if (leftCoords == null || rightCoords == null) {
            return Double.MAX_VALUE / 2;
        }
        return PositionUtil.getDistance(leftCoords[1], leftCoords[0], rightCoords[1], rightCoords[0]);
    }

    private List<Double> buildDistanceList(List<RouteSpotCandidate> orderedCandidateList) {
        List<Double> distanceList = new ArrayList<>();
        for (int i = 0; i < orderedCandidateList.size() - 1; i++) {
            distanceList.add(calculateDistance(orderedCandidateList.get(i).getSpot(), orderedCandidateList.get(i + 1).getSpot()));
        }
        return distanceList;
    }

    private BigDecimal sumCandidateCost(List<RouteSpotCandidate> candidateList) {
        return candidateList.stream()
                .map(RouteSpotCandidate::getEstimatedCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int sumVisitDuration(List<RouteSpotCandidate> candidateList) {
        return candidateList.stream()
                .map(RouteSpotCandidate::getSpot)
                .mapToInt(this::getVisitDurationMinutes)
                .sum();
    }

    private List<String> buildMatchedTagList(List<RouteSpotCandidate> candidateList, List<String> preferredTagList) {
        if (CollUtil.isEmpty(preferredTagList)) {
            return Collections.emptyList();
        }
        Set<String> matchedTagSet = new LinkedHashSet<>();
        candidateList.forEach(candidate -> matchedTagSet.addAll(candidate.getMatchedTagList()));
        return new ArrayList<>(matchedTagSet);
    }

    private List<SpotRoutePlanDayVO> buildDayPlanList(List<RouteSpotCandidate> orderedCandidateList, int dayCount, String paceType) {
        List<SpotRoutePlanDayVO> dayPlanList = new ArrayList<>();
        int currentIndex = 0;
        int maxDailyActivityMinutes = resolveMaxDailyActivityMinutes(paceType);
        int maxDailySpotCount = resolveMaxDailySpotCount(paceType);
        int breakMinutesBetweenSpots = resolveBreakMinutesBetweenSpots(paceType);
        for (int day = 1; day <= dayCount && currentIndex < orderedCandidateList.size(); day++) {
            int remainingDayCount = dayCount - day;
            List<RouteSpotCandidate> currentDayCandidateList = new ArrayList<>();
            int dayActivityMinutes = 0;

            while (currentIndex < orderedCandidateList.size()) {
                if (!currentDayCandidateList.isEmpty() && currentDayCandidateList.size() >= maxDailySpotCount) {
                    break;
                }
                RouteSpotCandidate nextCandidate = orderedCandidateList.get(currentIndex);
                int visitDurationMinutes = getVisitDurationMinutes(nextCandidate.getSpot());
                int transitMinutes = currentDayCandidateList.isEmpty()
                        ? 0
                        : estimateTransitMinutes(currentDayCandidateList.get(currentDayCandidateList.size() - 1).getSpot(),
                        nextCandidate.getSpot());
                int breakMinutes = currentDayCandidateList.isEmpty() ? 0 : breakMinutesBetweenSpots;
                int projectedMinutes = dayActivityMinutes + transitMinutes + breakMinutes + visitDurationMinutes;
                int remainingSpotCount = orderedCandidateList.size() - currentIndex;
                boolean needReserveForRemainingDays = remainingSpotCount <= remainingDayCount;

                if (!currentDayCandidateList.isEmpty()
                        && projectedMinutes > maxDailyActivityMinutes
                        && !needReserveForRemainingDays) {
                    break;
                }

                currentDayCandidateList.add(nextCandidate);
                dayActivityMinutes = projectedMinutes;
                currentIndex++;

                if (needReserveForRemainingDays) {
                    break;
                }
            }

            currentDayCandidateList = sortCandidatesWithinDay(currentDayCandidateList);
            SpotRoutePlanDayVO dayVO = new SpotRoutePlanDayVO();
            dayVO.setDayNumber(day);
            dayVO.setSpotList(currentDayCandidateList.stream().map(candidate -> {
                SpotVO spotVO = SpotVO.objToVo(candidate.getSpot());
                spotVO.setRecommendReason(candidate.getReason());
                spotVO.setRecommendedTimeBlock(resolveTimeBlockLabel(candidate.getPreferredTimeBlock()));
                return spotVO;
            }).collect(Collectors.toList()));
            dayVO.setSpotNameList(currentDayCandidateList.stream()
                    .map(candidate -> candidate.getSpot().getSpotName())
                    .collect(Collectors.toList()));
            List<Double> dayDistanceList = buildDistanceList(currentDayCandidateList);
            int totalVisitDurationMinutes = sumVisitDuration(currentDayCandidateList);
            int totalTransitMinutes = estimateDayTransitMinutes(currentDayCandidateList);
            List<ScheduleEntry> scheduleEntryList = buildScheduleEntryList(currentDayCandidateList, paceType);
            dayVO.setTotalDistance(dayDistanceList.stream().reduce(0D, Double::sum));
            dayVO.setTotalVisitDurationMinutes(totalVisitDurationMinutes);
            dayVO.setEstimatedCost(sumCandidateCost(currentDayCandidateList));
            dayVO.setScheduleLineList(scheduleEntryList.stream()
                    .map(ScheduleEntry::getDisplayLine)
                    .collect(Collectors.toList()));
            dayVO.setTimeBlockList(buildTimeBlockList(scheduleEntryList));
            dayVO.setIntensityLevel(resolveIntensityLevel(totalVisitDurationMinutes + totalTransitMinutes));
            dayVO.setRouteDescription(buildDayRouteDescription(day, dayVO));
            dayPlanList.add(dayVO);
        }
        return dayPlanList;
    }

    private String buildRouteDescription(String targetLocation,
                                         List<String> preferredTagList,
                                         BigDecimal budget,
                                         List<String> orderedSpotNameList,
                                         String paceType) {
        List<String> descriptionPartList = new ArrayList<>();
        if (StringUtils.isNotBlank(targetLocation)) {
            descriptionPartList.add("目的地偏向" + targetLocation);
        }
        if (CollUtil.isNotEmpty(preferredTagList)) {
            descriptionPartList.add("偏好标签为" + String.join("、", preferredTagList));
        }
        if (budget != null) {
            descriptionPartList.add("预算约" + budget.stripTrailingZeros().toPlainString() + "元");
        }
        if (CollUtil.isNotEmpty(orderedSpotNameList)) {
            descriptionPartList.add("推荐路线为" + String.join(" -> ", orderedSpotNameList));
        }
        descriptionPartList.add("当前节奏为" + resolvePaceLabel(paceType));
        descriptionPartList.add("会结合景点适合时段、建议游玩时长与景点间移动距离进行排程");
        return String.join("，", descriptionPartList);
    }

    private String buildSpotPlanReason(List<String> matchedTagList, Double avgScore, BigDecimal estimatedCost, BigDecimal budget) {
        if (CollUtil.isNotEmpty(matchedTagList)) {
            return "符合偏好标签：" + String.join("、", matchedTagList);
        }
        if (avgScore != null && avgScore >= 4.5D) {
            return "评分表现较好，适合纳入路线";
        }
        if (budget != null && estimatedCost.compareTo(BigDecimal.ZERO) > 0) {
            return "门票约" + estimatedCost.stripTrailingZeros().toPlainString() + "元";
        }
        return "综合热度与可玩性推荐";
    }

    private int getVisitDurationMinutes(Spot spot) {
        if (spot == null || spot.getVisitDurationMinutes() == null || spot.getVisitDurationMinutes() <= 0) {
            return DEFAULT_VISIT_DURATION_MINUTES;
        }
        return spot.getVisitDurationMinutes();
    }

    private int estimateTransitMinutes(Spot fromSpot, Spot toSpot) {
        double distance = calculateDistance(fromSpot, toSpot);
        if (distance >= Double.MAX_VALUE / 4) {
            return DEFAULT_TRANSIT_MINUTES;
        }
        int transitMinutes = (int) Math.ceil(distance / 25D * 60D);
        return Math.max(15, Math.min(transitMinutes, 180));
    }

    private int estimateDayTransitMinutes(List<RouteSpotCandidate> currentDayCandidateList) {
        if (currentDayCandidateList.size() <= 1) {
            return 0;
        }
        int totalTransitMinutes = 0;
        for (int i = 0; i < currentDayCandidateList.size() - 1; i++) {
            totalTransitMinutes += estimateTransitMinutes(currentDayCandidateList.get(i).getSpot(),
                    currentDayCandidateList.get(i + 1).getSpot());
        }
        return totalTransitMinutes;
    }

    private List<RouteSpotCandidate> sortCandidatesWithinDay(List<RouteSpotCandidate> candidateList) {
        if (candidateList.size() <= 1) {
            return candidateList;
        }
        Map<String, Integer> blockOrderMap = new HashMap<>();
        blockOrderMap.put(BLOCK_MORNING, 1);
        blockOrderMap.put(BLOCK_AFTERNOON, 2);
        blockOrderMap.put(BLOCK_EVENING, 3);
        return candidateList.stream()
                .sorted(Comparator
                        .comparingInt((RouteSpotCandidate candidate) -> blockOrderMap.getOrDefault(candidate.getPreferredTimeBlock(), 2))
                        .thenComparing(Comparator.comparingDouble(RouteSpotCandidate::getScore).reversed()))
                .collect(Collectors.toList());
    }

    private List<ScheduleEntry> buildScheduleEntryList(List<RouteSpotCandidate> currentDayCandidateList, String paceType) {
        if (CollUtil.isEmpty(currentDayCandidateList)) {
            return Collections.emptyList();
        }
        List<ScheduleEntry> scheduleEntryList = new ArrayList<>();
        int currentMinutes = DAY_START_MINUTES;
        Spot previousSpot = null;
        int breakMinutesBetweenSpots = resolveBreakMinutesBetweenSpots(paceType);
        for (int index = 0; index < currentDayCandidateList.size(); index++) {
            RouteSpotCandidate candidate = currentDayCandidateList.get(index);
            Spot currentSpot = candidate.getSpot();
            if (previousSpot != null) {
                int transitMinutes = estimateTransitMinutes(previousSpot, currentSpot);
                scheduleEntryList.add(new ScheduleEntry(currentMinutes, currentMinutes + transitMinutes,
                        formatTimeRange(currentMinutes, currentMinutes + transitMinutes) + " 路程交通",
                        "从" + previousSpot.getSpotName() + "前往" + currentSpot.getSpotName()));
                currentMinutes += transitMinutes;
            }
            int openMinutes = parseTimeToMinutes(currentSpot.getOpenTime(), DAY_START_MINUTES);
            int preferredStartMinutes = resolvePreferredStartMinutes(candidate.getPreferredTimeBlock(), currentMinutes);
            if (currentMinutes < preferredStartMinutes) {
                scheduleEntryList.add(new ScheduleEntry(currentMinutes, preferredStartMinutes,
                        formatTimeRange(currentMinutes, preferredStartMinutes) + " 机动 / 餐饮 / 调整节奏",
                        buildTimeAlignmentSummary(candidate.getPreferredTimeBlock(), currentSpot.getSpotName())));
                currentMinutes = preferredStartMinutes;
            }
            if (currentMinutes < openMinutes) {
                scheduleEntryList.add(new ScheduleEntry(currentMinutes, openMinutes,
                        formatTimeRange(currentMinutes, openMinutes) + " 机动 / 休息",
                        "预留机动时间，便于调整节奏"));
                currentMinutes = openMinutes;
            }
            int visitDurationMinutes = getVisitDurationMinutes(currentSpot);
            int closeMinutes = parseTimeToMinutes(currentSpot.getCloseTime(), 21 * 60);
            int endMinutes = Math.min(currentMinutes + visitDurationMinutes, closeMinutes);
            if (endMinutes <= currentMinutes) {
                endMinutes = currentMinutes + visitDurationMinutes;
            }
            scheduleEntryList.add(new ScheduleEntry(currentMinutes, endMinutes,
                    formatTimeRange(currentMinutes, endMinutes) + " 游玩 " + currentSpot.getSpotName(),
                    "重点游玩 " + currentSpot.getSpotName()));
            currentMinutes = endMinutes;
            if (breakMinutesBetweenSpots > 0 && index < currentDayCandidateList.size() - 1) {
                scheduleEntryList.add(new ScheduleEntry(currentMinutes, currentMinutes + breakMinutesBetweenSpots,
                        formatTimeRange(currentMinutes, currentMinutes + breakMinutesBetweenSpots) + " 休息 / 自由活动",
                        "预留休息和弹性调整时间"));
                currentMinutes += breakMinutesBetweenSpots;
            }
            previousSpot = currentSpot;
        }
        return scheduleEntryList;
    }

    private List<SpotRoutePlanTimeBlockVO> buildTimeBlockList(List<ScheduleEntry> scheduleEntryList) {
        if (CollUtil.isEmpty(scheduleEntryList)) {
            return Collections.emptyList();
        }
        Map<String, List<ScheduleEntry>> entryGroupMap = new LinkedHashMap<>();
        entryGroupMap.put("MORNING", new ArrayList<>());
        entryGroupMap.put("AFTERNOON", new ArrayList<>());
        entryGroupMap.put("EVENING", new ArrayList<>());
        for (ScheduleEntry scheduleEntry : scheduleEntryList) {
            entryGroupMap.get(resolveTimeBlockKey(scheduleEntry.getStartMinutes())).add(scheduleEntry);
        }

        List<SpotRoutePlanTimeBlockVO> timeBlockList = new ArrayList<>();
        for (Map.Entry<String, List<ScheduleEntry>> entry : entryGroupMap.entrySet()) {
            if (CollUtil.isEmpty(entry.getValue())) {
                continue;
            }
            SpotRoutePlanTimeBlockVO timeBlockVO = new SpotRoutePlanTimeBlockVO();
            timeBlockVO.setBlockKey(entry.getKey());
            timeBlockVO.setBlockLabel(resolveTimeBlockLabel(entry.getKey()));
            timeBlockVO.setSummary(buildTimeBlockSummary(entry.getKey(), entry.getValue()));
            timeBlockVO.setScheduleLineList(entry.getValue().stream()
                    .map(ScheduleEntry::getDisplayLine)
                    .collect(Collectors.toList()));
            timeBlockList.add(timeBlockVO);
        }
        return timeBlockList;
    }

    private String resolveTimeBlockKey(int startMinutes) {
        if (startMinutes < MORNING_END_MINUTES) {
            return BLOCK_MORNING;
        }
        if (startMinutes < AFTERNOON_END_MINUTES) {
            return BLOCK_AFTERNOON;
        }
        return BLOCK_EVENING;
    }

    private String resolveTimeBlockLabel(String blockKey) {
        switch (blockKey) {
            case BLOCK_MORNING:
                return "上午";
            case BLOCK_AFTERNOON:
                return "下午";
            case BLOCK_EVENING:
                return "晚上";
            default:
                return "当日";
        }
    }

    private String buildTimeBlockSummary(String blockKey, List<ScheduleEntry> scheduleEntryList) {
        List<String> highlightList = scheduleEntryList.stream()
                .map(ScheduleEntry::getSummary)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .limit(3)
                .collect(Collectors.toList());
        String prefix = resolveTimeBlockLabel(blockKey);
        if (CollUtil.isEmpty(highlightList)) {
            return prefix + "以机动安排为主";
        }
        return prefix + "建议：" + String.join("，", highlightList);
    }

    private int parseTimeToMinutes(String timeText, int defaultMinutes) {
        if (StringUtils.isBlank(timeText) || !timeText.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            return defaultMinutes;
        }
        String[] parts = timeText.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private String formatTimeRange(int startMinutes, int endMinutes) {
        return formatClock(startMinutes) + "-" + formatClock(endMinutes);
    }

    private String formatClock(int totalMinutes) {
        int normalizedMinutes = Math.max(totalMinutes, 0);
        int hour = normalizedMinutes / 60;
        int minute = normalizedMinutes % 60;
        return String.format("%02d:%02d", hour, minute);
    }

    private String resolveIntensityLevel(int totalActivityMinutes) {
        if (totalActivityMinutes <= 5 * 60) {
            return "轻松";
        }
        if (totalActivityMinutes <= 8 * 60) {
            return "适中";
        }
        return "紧凑";
    }

    private String buildDayRouteDescription(int dayNumber, SpotRoutePlanDayVO dayVO) {
        String spotSummary = CollUtil.isEmpty(dayVO.getSpotNameList())
                ? "暂无景点"
                : String.join(" -> ", dayVO.getSpotNameList());
        return "第" + dayNumber + "天建议游玩：" + spotSummary
                + "；预计游玩" + dayVO.getTotalVisitDurationMinutes() + "分钟"
                + "；行程强度" + StringUtils.defaultIfBlank(dayVO.getIntensityLevel(), "适中");
    }

    private String resolvePreferredTimeBlock(Spot spot, List<String> tagList) {
        String spotName = spot == null ? "" : StringUtils.defaultString(spot.getSpotName());
        String description = spot == null ? "" : StringUtils.defaultString(spot.getSpotDescription());
        if (containsAnyIgnoreCase(spotName, description, "夜", "灯光", "步行街", "观景台", "夜景", "步行桥")
                || tagList.stream().anyMatch(tag -> containsAnyIgnoreCase(tag, "", "夜游", "夜景", "灯光", "观景", "城市地标"))) {
            return BLOCK_EVENING;
        }
        if (containsAnyIgnoreCase(spotName, description, "博物", "纪念馆", "总统府", "展馆")
                || tagList.stream().anyMatch(tag -> containsAnyIgnoreCase(tag, "", "博物馆", "纪念馆", "民国建筑", "历史文化"))) {
            return BLOCK_AFTERNOON;
        }
        if (containsAnyIgnoreCase(spotName, description, "山", "陵", "寺", "公园", "动物园", "湖", "湿地")
                || tagList.stream().anyMatch(tag -> containsAnyIgnoreCase(tag, "", "自然风光", "陵寝", "佛教文化", "亲子", "湖泊", "山水"))) {
            return BLOCK_MORNING;
        }
        return BLOCK_AFTERNOON;
    }

    private boolean containsAnyIgnoreCase(String primaryText, String secondaryText, String... keywords) {
        for (String keyword : keywords) {
            if (StringUtils.containsIgnoreCase(primaryText, keyword)
                    || StringUtils.containsIgnoreCase(secondaryText, keyword)) {
                return true;
            }
        }
        return false;
    }

    private int resolvePreferredStartMinutes(String preferredTimeBlock, int currentMinutes) {
        if (BLOCK_AFTERNOON.equals(preferredTimeBlock)) {
            return Math.max(currentMinutes, AFTERNOON_START_MINUTES);
        }
        if (BLOCK_EVENING.equals(preferredTimeBlock)) {
            return Math.max(currentMinutes, EVENING_START_MINUTES);
        }
        return currentMinutes;
    }

    private String buildTimeAlignmentSummary(String preferredTimeBlock, String spotName) {
        if (BLOCK_AFTERNOON.equals(preferredTimeBlock)) {
            return "为 " + spotName + " 预留更适合的下午参观时段";
        }
        if (BLOCK_EVENING.equals(preferredTimeBlock)) {
            return "为 " + spotName + " 预留更适合的夜间体验时段";
        }
        return "为后续景点预留更合适的游玩时段";
    }

    private double buildPacePreferenceScore(List<String> tagList,
                                            List<String> matchedTagList,
                                            BigDecimal estimatedCost,
                                            BigDecimal budget,
                                            String paceType,
                                            Spot spot) {
        String normalizedPaceType = normalizePaceType(paceType);
        boolean nightViewSpot = isNightViewSpot(tagList);
        boolean userPrefersNightView = matchedTagList.contains("夜游");
        int visitDurationMinutes = getVisitDurationMinutes(spot);
        double score = 0D;

        if (PACE_RELAXED.equals(normalizedPaceType)) {
            if (visitDurationMinutes >= 120 && visitDurationMinutes <= 240) {
                score += 1.8D;
            } else if (visitDurationMinutes > 300) {
                score -= 1.2D;
            }
            if (nightViewSpot && !userPrefersNightView) {
                score -= 2.4D;
            }
            if (estimatedCost.compareTo(BigDecimal.ZERO) == 0) {
                score += 0.6D;
            }
            return score;
        }

        if (PACE_COMPACT.equals(normalizedPaceType)) {
            if (visitDurationMinutes <= 90) {
                score += 2.5D;
            } else if (visitDurationMinutes <= 150) {
                score += 1.5D;
            } else if (visitDurationMinutes >= 300) {
                score -= 1.6D;
            }
            if (nightViewSpot) {
                score += 2.2D;
            }
            if (budget == null || estimatedCost.compareTo(BigDecimal.ZERO) == 0) {
                score += 0.8D;
            }
            return score;
        }

        if (visitDurationMinutes >= 90 && visitDurationMinutes <= 210) {
            score += 0.8D;
        }
        if (nightViewSpot && userPrefersNightView) {
            score += 1.0D;
        }
        return score;
    }

    private boolean isNightViewSpot(List<String> tagList) {
        return tagList.stream().anyMatch(tag -> StringUtils.containsAnyIgnoreCase(tag, "夜", "灯光", "晚"));
    }

    private String normalizePaceType(String paceType) {
        if (StringUtils.isBlank(paceType)) {
            return PACE_NORMAL;
        }
        String normalized = StringUtils.upperCase(paceType.trim());
        if (PACE_RELAXED.equals(normalized) || PACE_COMPACT.equals(normalized)) {
            return normalized;
        }
        return PACE_NORMAL;
    }

    private String resolvePaceLabel(String paceType) {
        String normalizedPaceType = normalizePaceType(paceType);
        if (PACE_RELAXED.equals(normalizedPaceType)) {
            return "轻松";
        }
        if (PACE_COMPACT.equals(normalizedPaceType)) {
            return "紧凑";
        }
        return "常规";
    }

    private int resolveMaxDailyActivityMinutes(String paceType) {
        String normalizedPaceType = normalizePaceType(paceType);
        if (PACE_RELAXED.equals(normalizedPaceType)) {
            return 7 * 60;
        }
        if (PACE_COMPACT.equals(normalizedPaceType)) {
            return 11 * 60;
        }
        return MAX_DAILY_ACTIVITY_MINUTES;
    }

    private int resolveMaxDailySpotCount(String paceType) {
        String normalizedPaceType = normalizePaceType(paceType);
        if (PACE_RELAXED.equals(normalizedPaceType)) {
            return 2;
        }
        if (PACE_COMPACT.equals(normalizedPaceType)) {
            return 4;
        }
        return 3;
    }

    private int resolveBreakMinutesBetweenSpots(String paceType) {
        String normalizedPaceType = normalizePaceType(paceType);
        if (PACE_RELAXED.equals(normalizedPaceType)) {
            return 30;
        }
        if (PACE_COMPACT.equals(normalizedPaceType)) {
            return 0;
        }
        return 15;
    }

    private double resolveAverageSpotCountPerDay(String paceType) {
        String normalizedPaceType = normalizePaceType(paceType);
        if (PACE_RELAXED.equals(normalizedPaceType)) {
            return 1.5D;
        }
        if (PACE_COMPACT.equals(normalizedPaceType)) {
            return 3D;
        }
        return 2D;
    }

    private SpotRoutePlanRequest copyPlanRequest(SpotRoutePlanRequest originalRequest) {
        SpotRoutePlanRequest copiedRequest = new SpotRoutePlanRequest();
        copiedRequest.setDayCount(originalRequest.getDayCount());
        copiedRequest.setBudget(originalRequest.getBudget());
        copiedRequest.setSpotLocation(originalRequest.getSpotLocation());
        copiedRequest.setPaceType(originalRequest.getPaceType());
        copiedRequest.setSpotTagList(CollUtil.isEmpty(originalRequest.getSpotTagList())
                ? new ArrayList<>()
                : new ArrayList<>(originalRequest.getSpotTagList()));
        copiedRequest.setExcludeTagList(CollUtil.isEmpty(originalRequest.getExcludeTagList())
                ? new ArrayList<>()
                : new ArrayList<>(originalRequest.getExcludeTagList()));
        copiedRequest.setCandidateSpotIdList(CollUtil.isEmpty(originalRequest.getCandidateSpotIdList())
                ? new ArrayList<>()
                : new ArrayList<>(originalRequest.getCandidateSpotIdList()));
        return copiedRequest;
    }

    private String applyAdjustment(SpotRoutePlanRequest adjustedRequest, String adjustType, BigDecimal currentEstimatedCost) {
        switch (adjustType) {
            case ADJUST_RELAXED:
                adjustedRequest.setPaceType(PACE_RELAXED);
                return "已按更轻松的节奏重新规划，每日景点数量会更克制。";
            case ADJUST_SAVE_BUDGET:
                BigDecimal targetBudget = deriveBudgetTarget(adjustedRequest.getBudget(), currentEstimatedCost);
                adjustedRequest.setBudget(targetBudget);
                adjustedRequest.setPaceType(PACE_RELAXED);
                return "已按更省钱方案重新规划，优先选择更低成本且更轻松的行程。";
            case ADJUST_ADD_NIGHT_VIEW:
                List<String> tagList = CollUtil.emptyIfNull(adjustedRequest.getSpotTagList());
                if (!tagList.contains("夜游")) {
                    tagList = new ArrayList<>(tagList);
                    tagList.add("夜游");
                }
                adjustedRequest.setSpotTagList(tagList);
                return "已增加夜游偏好，优先纳入更适合晚间体验的景点。";
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的微调类型");
        }
    }

    private String applyNaturalLanguageAdjustment(SpotRoutePlanRequest adjustedRequest,
                                                  RouteAdjustmentIntent adjustmentIntent,
                                                  BigDecimal currentEstimatedCost) {
        List<String> summaryList = new ArrayList<>();
        boolean adjusted = false;

        if (StringUtils.isNotBlank(adjustmentIntent.getPaceType())
                && !"KEEP".equalsIgnoreCase(adjustmentIntent.getPaceType())) {
            adjustedRequest.setPaceType(normalizePaceType(adjustmentIntent.getPaceType()));
            summaryList.add("已调整为" + resolvePaceLabel(adjustedRequest.getPaceType()) + "节奏");
            adjusted = true;
        }

        if ("SAVE".equalsIgnoreCase(adjustmentIntent.getBudgetAction())) {
            adjustedRequest.setBudget(deriveBudgetTarget(adjustedRequest.getBudget(), currentEstimatedCost));
            if (StringUtils.isBlank(adjustedRequest.getPaceType()) || PACE_NORMAL.equals(normalizePaceType(adjustedRequest.getPaceType()))) {
                adjustedRequest.setPaceType(PACE_RELAXED);
            }
            summaryList.add("已按更省钱方案重新控制预算");
            adjusted = true;
        }

        List<String> nextTagList = mergeTagList(adjustedRequest.getSpotTagList(), adjustmentIntent.getIncludeTags());
        if (!nextTagList.equals(CollUtil.emptyIfNull(adjustedRequest.getSpotTagList()))) {
            adjustedRequest.setSpotTagList(nextTagList);
            summaryList.add("已补充偏好标签：" + String.join("、", adjustmentIntent.getIncludeTags()));
            adjusted = true;
        }

        List<String> nextExcludeTagList = mergeTagList(adjustedRequest.getExcludeTagList(), adjustmentIntent.getExcludeTags());
        if (!nextExcludeTagList.equals(CollUtil.emptyIfNull(adjustedRequest.getExcludeTagList()))) {
            adjustedRequest.setExcludeTagList(nextExcludeTagList);
            summaryList.add("已排除标签：" + String.join("、", adjustmentIntent.getExcludeTags()));
            adjusted = true;
        }

        ThrowUtils.throwIf(!adjusted, ErrorCode.PARAMS_ERROR, "暂未识别该改线指令，可尝试输入更明确的描述");
        return "已根据你的描述重新规划：" + String.join("，", summaryList);
    }

    private boolean appendTagIfMentioned(String instruction, List<String> targetTagList, String tag, String[] keywords) {
        if (!containsAnyIgnoreCase(instruction, "", keywords)) {
            return false;
        }
        if (!targetTagList.contains(tag)) {
            targetTagList.add(tag);
        }
        return true;
    }

    private RouteAdjustmentIntent parseNaturalLanguageAdjustmentIntent(String instruction,
                                                                       SpotRoutePlanRequest originalRequest,
                                                                       BigDecimal currentEstimatedCost) {
        if (tourismAiClient.isAvailable()) {
            try {
                return parseNaturalLanguageAdjustmentIntentByAi(instruction, originalRequest, currentEstimatedCost);
            } catch (Exception e) {
                log.warn("AI 解析改线指令失败，降级使用规则解析，instruction={}", instruction, e);
            }
        }
        return parseNaturalLanguageAdjustmentIntentByRule(instruction);
    }

    private RouteAdjustmentIntent parseNaturalLanguageAdjustmentIntentByAi(String instruction,
                                                                           SpotRoutePlanRequest originalRequest,
                                                                           BigDecimal currentEstimatedCost) {
        String systemPrompt = "你是旅游路线改线意图解析器。请把用户的一句话改线需求解析成 JSON，不要输出 markdown、解释或代码块。"
                + "JSON 字段固定为：summary, paceType, budgetAction, includeTags, excludeTags, actionList。"
                + "其中 paceType 只能取 RELAXED、NORMAL、COMPACT、KEEP；budgetAction 只能取 KEEP、SAVE。"
                + "includeTags / excludeTags 只允许从这些标签中选择：夜游、美食、摄影、亲子、历史文化、自然风光、博物馆、纪念馆、寺庙、古镇、古街、园林、湖泊、城市地标、观景。";
        String userPrompt = new StringBuilder()
                .append("当前路线请求：")
                .append(JSONUtil.toJsonStr(originalRequest))
                .append("\n当前总花费：")
                .append(currentEstimatedCost == null ? "未知" : currentEstimatedCost.stripTrailingZeros().toPlainString())
                .append("\n用户改线描述：")
                .append(instruction)
                .append("\n请只返回 JSON，例如：")
                .append("{\"summary\":\"用户希望更轻松并加入夜景\",\"paceType\":\"RELAXED\",\"budgetAction\":\"KEEP\",\"includeTags\":[\"夜游\"],\"excludeTags\":[\"博物馆\"],\"actionList\":[\"放缓节奏\",\"加入夜景\",\"排除博物馆\"]}")
                .toString();
        String aiContent = tourismAiClient.chat(systemPrompt, userPrompt);
        return parseRouteAdjustmentIntentJson(cleanJsonText(aiContent), instruction);
    }

    private RouteAdjustmentIntent parseNaturalLanguageAdjustmentIntentByRule(String instruction) {
        String normalizedInstruction = instruction.replace("，", " ").replace(",", " ");
        RouteAdjustmentIntent intent = new RouteAdjustmentIntent();
        intent.setOriginalInstruction(instruction);
        intent.setPaceType("KEEP");
        intent.setBudgetAction("KEEP");
        intent.setIncludeTags(new ArrayList<>());
        intent.setExcludeTags(new ArrayList<>());
        intent.setActionList(new ArrayList<>());

        if (containsAnyIgnoreCase(normalizedInstruction, "", "太赶", "轻松", "不要太累", "悠闲", "慢一点")) {
            intent.setPaceType(PACE_RELAXED);
            intent.getActionList().add("放缓节奏");
        } else if (containsAnyIgnoreCase(normalizedInstruction, "", "紧凑", "多玩", "多去几个", "充实", "高密度")) {
            intent.setPaceType(PACE_COMPACT);
            intent.getActionList().add("提升游玩密度");
        }

        if (containsAnyIgnoreCase(normalizedInstruction, "", "预算", "省钱", "便宜", "花费太高", "控制预算")) {
            intent.setBudgetAction("SAVE");
            intent.getActionList().add("控制预算");
        }

        appendTagIfMentioned(normalizedInstruction, intent.getIncludeTags(), "夜游", new String[]{"夜游", "夜景", "晚上逛", "夜间"});
        appendTagIfMentioned(normalizedInstruction, intent.getIncludeTags(), "美食", new String[]{"美食", "小吃", "吃饭", "夜宵"});
        appendTagIfMentioned(normalizedInstruction, intent.getIncludeTags(), "摄影", new String[]{"拍照", "摄影", "出片", "打卡"});
        appendTagIfMentioned(normalizedInstruction, intent.getIncludeTags(), "亲子", new String[]{"亲子", "带孩子", "小朋友", "儿童"});
        appendTagIfMentioned(normalizedInstruction, intent.getIncludeTags(), "历史文化", new String[]{"历史", "文化", "人文"});
        appendTagIfMentioned(normalizedInstruction, intent.getIncludeTags(), "自然风光", new String[]{"自然", "风景", "山水", "公园"});
        appendExcludeTagIfMentioned(normalizedInstruction, intent.getExcludeTags(), "博物馆", new String[]{"不要博物馆", "不想去博物馆", "别去博物馆"});
        appendExcludeTagIfMentioned(normalizedInstruction, intent.getExcludeTags(), "纪念馆", new String[]{"不要纪念馆", "不想去纪念馆"});
        appendExcludeTagIfMentioned(normalizedInstruction, intent.getExcludeTags(), "夜游", new String[]{"不要夜游", "不想夜游", "不想晚上逛"});

        if (CollUtil.isEmpty(intent.getActionList())) {
            if (CollUtil.isNotEmpty(intent.getIncludeTags())) {
                intent.getActionList().add("补充偏好标签");
            }
            if (CollUtil.isNotEmpty(intent.getExcludeTags())) {
                intent.getActionList().add("排除部分景点类型");
            }
        }
        intent.setSummary(buildRuleIntentSummary(intent));
        return intent;
    }

    private RouteAdjustmentIntent parseRouteAdjustmentIntentJson(String jsonText, String originalInstruction) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonText);
        RouteAdjustmentIntent intent = new RouteAdjustmentIntent();
        intent.setOriginalInstruction(originalInstruction);
        intent.setSummary(StringUtils.defaultIfBlank(jsonObject.getStr("summary"), "已理解用户的改线诉求"));
        intent.setPaceType(StringUtils.defaultIfBlank(jsonObject.getStr("paceType"), "KEEP"));
        intent.setBudgetAction(StringUtils.defaultIfBlank(jsonObject.getStr("budgetAction"), "KEEP"));
        intent.setIncludeTags(normalizeTagList(jsonObject.getJSONArray("includeTags")));
        intent.setExcludeTags(normalizeTagList(jsonObject.getJSONArray("excludeTags")));
        intent.setActionList(normalizeStringList(jsonObject.getJSONArray("actionList")));
        return intent;
    }

    private String cleanJsonText(String content) {
        String cleaned = StringUtils.trimToEmpty(content);
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceFirst("^```json", "").replaceFirst("^```", "").replaceAll("```$", "").trim();
        }
        return cleaned;
    }

    private List<String> normalizeTagList(Object arrayValue) {
        List<String> rawList = normalizeStringList(arrayValue);
        List<String> supportedTagList = new ArrayList<>();
        List<String> supportedPool = new ArrayList<>();
        Collections.addAll(supportedPool, "夜游", "美食", "摄影", "亲子", "历史文化", "自然风光",
                "博物馆", "纪念馆", "寺庙", "古镇", "古街", "园林", "湖泊", "城市地标", "观景");
        for (String item : rawList) {
            String normalized = StringUtils.trimToEmpty(item);
            for (String supportedTag : supportedPool) {
                if (StringUtils.equalsIgnoreCase(normalized, supportedTag) || StringUtils.containsIgnoreCase(normalized, supportedTag)) {
                    if (!supportedTagList.contains(supportedTag)) {
                        supportedTagList.add(supportedTag);
                    }
                    break;
                }
            }
        }
        return supportedTagList;
    }

    private List<String> normalizeStringList(Object arrayValue) {
        if (arrayValue == null) {
            return new ArrayList<>();
        }
        try {
            return JSONUtil.toList(JSONUtil.parseArray(arrayValue), String.class).stream()
                    .filter(StringUtils::isNotBlank)
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String buildRuleIntentSummary(RouteAdjustmentIntent intent) {
        List<String> summaryList = new ArrayList<>();
        if (!"KEEP".equalsIgnoreCase(intent.getPaceType())) {
            summaryList.add("希望调整路线节奏");
        }
        if ("SAVE".equalsIgnoreCase(intent.getBudgetAction())) {
            summaryList.add("希望控制预算");
        }
        if (CollUtil.isNotEmpty(intent.getIncludeTags())) {
            summaryList.add("想增加" + String.join("、", intent.getIncludeTags()) + "相关体验");
        }
        if (CollUtil.isNotEmpty(intent.getExcludeTags())) {
            summaryList.add("希望避开" + String.join("、", intent.getExcludeTags()) + "类型景点");
        }
        if (CollUtil.isEmpty(summaryList)) {
            return "已尝试理解用户的改线诉求";
        }
        return String.join("，", summaryList);
    }

    private RouteAdjustmentAnalysisVO buildAdjustmentAnalysisVO(RouteAdjustmentIntent intent) {
        RouteAdjustmentAnalysisVO analysisVO = new RouteAdjustmentAnalysisVO();
        analysisVO.setOriginalInstruction(intent.getOriginalInstruction());
        analysisVO.setInterpretedSummary(intent.getSummary());
        analysisVO.setActionList(intent.getActionList());
        analysisVO.setIncludeTagList(intent.getIncludeTags());
        analysisVO.setExcludeTagList(intent.getExcludeTags());
        analysisVO.setSuggestedPaceType("KEEP".equalsIgnoreCase(intent.getPaceType()) ? null : resolvePaceLabel(intent.getPaceType()));
        return analysisVO;
    }

    private List<String> mergeTagList(List<String> originalTagList, List<String> extraTagList) {
        List<String> mergedTagList = new ArrayList<>(CollUtil.emptyIfNull(originalTagList));
        for (String tag : CollUtil.emptyIfNull(extraTagList)) {
            if (StringUtils.isNotBlank(tag) && !mergedTagList.contains(tag)) {
                mergedTagList.add(tag);
            }
        }
        return mergedTagList;
    }

    private boolean appendExcludeTagIfMentioned(String instruction, List<String> targetTagList, String tag, String[] keywords) {
        if (!containsAnyIgnoreCase(instruction, "", keywords)) {
            return false;
        }
        if (!targetTagList.contains(tag)) {
            targetTagList.add(tag);
        }
        return true;
    }

    private BigDecimal deriveBudgetTarget(BigDecimal originalBudget, BigDecimal currentEstimatedCost) {
        BigDecimal budgetBase = originalBudget;
        if (budgetBase == null || budgetBase.compareTo(BigDecimal.ZERO) <= 0) {
            budgetBase = currentEstimatedCost;
        }
        if (budgetBase == null || budgetBase.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.valueOf(500);
        }
        BigDecimal targetBudget = budgetBase.multiply(BigDecimal.valueOf(0.8)).setScale(0, RoundingMode.DOWN);
        return targetBudget.max(BigDecimal.valueOf(200));
    }

    private List<String> parseTagList(String spotTags) {
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

    private static class RouteSpotCandidate {
        private final Spot spot;
        private final double score;
        private final BigDecimal estimatedCost;
        private final List<String> matchedTagList;
        private final String reason;
        private final String preferredTimeBlock;

        private RouteSpotCandidate(Spot spot,
                                   double score,
                                   BigDecimal estimatedCost,
                                   List<String> matchedTagList,
                                   String reason,
                                   String preferredTimeBlock) {
            this.spot = spot;
            this.score = score;
            this.estimatedCost = estimatedCost;
            this.matchedTagList = matchedTagList;
            this.reason = reason;
            this.preferredTimeBlock = preferredTimeBlock;
        }

        public Spot getSpot() {
            return spot;
        }

        public double getScore() {
            return score;
        }

        public BigDecimal getEstimatedCost() {
            return estimatedCost;
        }

        public List<String> getMatchedTagList() {
            return matchedTagList;
        }

        public String getReason() {
            return reason;
        }

        public String getPreferredTimeBlock() {
            return preferredTimeBlock;
        }
    }

    private static class RouteAdjustmentIntent {

        private String originalInstruction;

        private String summary;

        private String paceType;

        private String budgetAction;

        private List<String> includeTags;

        private List<String> excludeTags;

        private List<String> actionList;

        public String getOriginalInstruction() {
            return originalInstruction;
        }

        public void setOriginalInstruction(String originalInstruction) {
            this.originalInstruction = originalInstruction;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getPaceType() {
            return paceType;
        }

        public void setPaceType(String paceType) {
            this.paceType = paceType;
        }

        public String getBudgetAction() {
            return budgetAction;
        }

        public void setBudgetAction(String budgetAction) {
            this.budgetAction = budgetAction;
        }

        public List<String> getIncludeTags() {
            return includeTags;
        }

        public void setIncludeTags(List<String> includeTags) {
            this.includeTags = includeTags;
        }

        public List<String> getExcludeTags() {
            return excludeTags;
        }

        public void setExcludeTags(List<String> excludeTags) {
            this.excludeTags = excludeTags;
        }

        public List<String> getActionList() {
            return actionList;
        }

        public void setActionList(List<String> actionList) {
            this.actionList = actionList;
        }
    }

    private static class ScheduleEntry {

        private final int startMinutes;

        private final int endMinutes;

        private final String displayLine;

        private final String summary;

        private ScheduleEntry(int startMinutes, int endMinutes, String displayLine, String summary) {
            this.startMinutes = startMinutes;
            this.endMinutes = endMinutes;
            this.displayLine = displayLine;
            this.summary = summary;
        }

        public int getStartMinutes() {
            return startMinutes;
        }

        public int getEndMinutes() {
            return endMinutes;
        }

        public String getDisplayLine() {
            return displayLine;
        }

        public String getSummary() {
            return summary;
        }
    }

}
