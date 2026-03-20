package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.constant.CommonConstant;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.mapper.SpotFeeMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotRouteMapper;
import com.xiaobaitiao.springbootinit.mapper.SpotScoreMapper;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRouteQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRoutePlanRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.SpotFee;
import com.xiaobaitiao.springbootinit.model.entity.SpotRoute;
import com.xiaobaitiao.springbootinit.model.entity.SpotScore;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanDayVO;
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


    @Resource
    private SpotService spotService;

    @Resource
    private SpotFeeMapper spotFeeMapper;

    @Resource
    private SpotScoreMapper spotScoreMapper;

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
                    // 解析经纬度
                    double[] currentCoords = parseLocation(currentSpot.getSpotLocation());
                    double[] nextCoords = parseLocation(nextSpot.getSpotLocation());

                    if (currentCoords != null && nextCoords != null) {
                        double distance = PositionUtil.getDistance(
                                currentCoords[1], currentCoords[0], // 当前景点的经度、纬度
                                nextCoords[1], nextCoords[0]        // 下一个景点的经度、纬度
                        );
                        spotDistanceList.add(distance);
                    } else {
                        spotDistanceList.add(0.0); // 无法解析经纬度时，默认距离为0
                    }
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
                        // 解析经纬度
                        double[] currentCoords = parseLocation(currentSpot.getSpotLocation());
                        double[] nextCoords = parseLocation(nextSpot.getSpotLocation());

                        if (currentCoords != null && nextCoords != null) {
                            double distance = PositionUtil.getDistance(
                                    currentCoords[1], currentCoords[0], // 当前景点的经度、纬度
                                    nextCoords[1], nextCoords[0]        // 下一个景点的经度、纬度
                            );
                            spotDistanceList.add(distance);
                        } else {
                            spotDistanceList.add(0.0); // 无法解析经纬度时，默认距离为0
                        }
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
        String targetLocation = StringUtils.trimToEmpty(spotRoutePlanRequest.getSpotLocation());
        List<String> preferredTagList = CollUtil.emptyIfNull(spotRoutePlanRequest.getSpotTagList()).stream()
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
                .map(spot -> buildRouteSpotCandidate(spot, preferredTagList, minPriceMap, avgScoreMap, maxViewNum, maxFavourNum, budget, dayCount))
                .sorted(Comparator.comparingDouble(RouteSpotCandidate::getScore).reversed())
                .collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(routeSpotCandidateList), ErrorCode.NOT_FOUND_ERROR, "暂无符合条件的景点");

        List<RouteSpotCandidate> selectedCandidateList = selectRouteCandidates(routeSpotCandidateList, budget, dayCount);
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
        planVO.setRouteDescription(buildRouteDescription(targetLocation, preferredTagList, budget, orderedSpotNameList));
        planVO.setTotalDays(dayCount);
        planVO.setTotalSpotCount(orderedCandidateList.size());
        planVO.setTotalEstimatedCost(sumCandidateCost(orderedCandidateList));
        planVO.setCoverSpotAvatar(orderedCandidateList.get(0).getSpot().getSpotAvatar());
        planVO.setSpotIdList(orderedSpotIdList);
        planVO.setSpotNameList(orderedSpotNameList);
        planVO.setSpotDistanceList(spotDistanceList);
        planVO.setMatchedTagList(buildMatchedTagList(orderedCandidateList, preferredTagList));
        planVO.setSpotList(orderedSpotVOList);
        planVO.setDayPlanList(buildDayPlanList(orderedCandidateList, dayCount));
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
    }

    private boolean matchLocation(Spot spot, String targetLocation) {
        if (StringUtils.isBlank(targetLocation)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(StringUtils.defaultString(spot.getSpotLocation()), targetLocation);
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
                                                       int dayCount) {
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

        double score = tagScore + ratingScore + hotScore + budgetFriendlyScore;
        return new RouteSpotCandidate(spot, score, estimatedCost, matchedTagList,
                buildSpotPlanReason(matchedTagList, avgScore, estimatedCost, budget));
    }

    private List<RouteSpotCandidate> selectRouteCandidates(List<RouteSpotCandidate> candidateList,
                                                           BigDecimal budget,
                                                           int dayCount) {
        int expectedSpotCount = Math.min(Math.max(dayCount * 3, dayCount), candidateList.size());
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
        double[] leftCoords = parseLocation(leftSpot.getSpotLocation());
        double[] rightCoords = parseLocation(rightSpot.getSpotLocation());
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

    private List<String> buildMatchedTagList(List<RouteSpotCandidate> candidateList, List<String> preferredTagList) {
        if (CollUtil.isEmpty(preferredTagList)) {
            return Collections.emptyList();
        }
        Set<String> matchedTagSet = new LinkedHashSet<>();
        candidateList.forEach(candidate -> matchedTagSet.addAll(candidate.getMatchedTagList()));
        return new ArrayList<>(matchedTagSet);
    }

    private List<SpotRoutePlanDayVO> buildDayPlanList(List<RouteSpotCandidate> orderedCandidateList, int dayCount) {
        List<SpotRoutePlanDayVO> dayPlanList = new ArrayList<>();
        int currentIndex = 0;
        for (int day = 1; day <= dayCount && currentIndex < orderedCandidateList.size(); day++) {
            int remainingSpotCount = orderedCandidateList.size() - currentIndex;
            int remainingDayCount = dayCount - day + 1;
            int currentDaySpotCount = (int) Math.ceil(remainingSpotCount * 1.0 / remainingDayCount);
            List<RouteSpotCandidate> currentDayCandidateList = new ArrayList<>(
                    orderedCandidateList.subList(currentIndex, currentIndex + currentDaySpotCount));
            currentIndex += currentDaySpotCount;

            SpotRoutePlanDayVO dayVO = new SpotRoutePlanDayVO();
            dayVO.setDayNumber(day);
            dayVO.setSpotList(currentDayCandidateList.stream().map(candidate -> {
                SpotVO spotVO = SpotVO.objToVo(candidate.getSpot());
                spotVO.setRecommendReason(candidate.getReason());
                return spotVO;
            }).collect(Collectors.toList()));
            dayVO.setSpotNameList(currentDayCandidateList.stream()
                    .map(candidate -> candidate.getSpot().getSpotName())
                    .collect(Collectors.toList()));
            dayVO.setTotalDistance(buildDistanceList(currentDayCandidateList).stream().reduce(0D, Double::sum));
            dayVO.setEstimatedCost(sumCandidateCost(currentDayCandidateList));
            dayVO.setRouteDescription("第" + day + "天建议游玩：" + String.join(" -> ", dayVO.getSpotNameList()));
            dayPlanList.add(dayVO);
        }
        return dayPlanList;
    }

    private String buildRouteDescription(String targetLocation,
                                         List<String> preferredTagList,
                                         BigDecimal budget,
                                         List<String> orderedSpotNameList) {
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

        private RouteSpotCandidate(Spot spot, double score, BigDecimal estimatedCost, List<String> matchedTagList, String reason) {
            this.spot = spot;
            this.score = score;
            this.estimatedCost = estimatedCost;
            this.matchedTagList = matchedTagList;
            this.reason = reason;
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
    }

}
