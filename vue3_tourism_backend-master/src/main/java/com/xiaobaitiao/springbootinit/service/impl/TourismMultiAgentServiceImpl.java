package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.model.dto.tourismAgent.TourismMultiAgentRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.model.vo.TourismMultiAgentVO;
import com.xiaobaitiao.springbootinit.model.vo.TourismPreferenceVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.TourismMultiAgentService;
import com.xiaobaitiao.springbootinit.service.agent.TourismExplanationAgentService;
import com.xiaobaitiao.springbootinit.service.agent.TourismPlanningAgentService;
import com.xiaobaitiao.springbootinit.service.agent.TourismRecommendAgentService;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 多智能协作旅游服务实现
 */
@Service
public class TourismMultiAgentServiceImpl implements TourismMultiAgentService {

    private static final Pattern DAY_PATTERN = Pattern.compile("(\\d+)\\s*天");
    private static final Pattern BUDGET_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*元");
    private static final List<String> DEFAULT_TAG_LIST = Arrays.asList(
            "自然风光", "历史文化", "休闲娱乐", "亲子", "古镇", "美食", "爬山", "拍照", "海边", "博物馆");

    @Resource
    private SpotService spotService;

    @Resource
    private TourismRecommendAgentService tourismRecommendAgentService;

    @Resource
    private TourismPlanningAgentService tourismPlanningAgentService;

    @Resource
    private TourismExplanationAgentService tourismExplanationAgentService;

    @Override
    public TourismMultiAgentVO generatePlan(TourismMultiAgentRequest tourismMultiAgentRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(tourismMultiAgentRequest == null, ErrorCode.PARAMS_ERROR);
        TourismAgentContext context = buildContext(tourismMultiAgentRequest);
        List<SpotVO> recommendationSpotList = tourismRecommendAgentService.recommend(context, request);
        SpotRoutePlanVO routePlan = tourismPlanningAgentService.plan(context, recommendationSpotList, request);
        String explanationMarkdown = tourismExplanationAgentService.explain(context, recommendationSpotList, routePlan);

        TourismMultiAgentVO result = new TourismMultiAgentVO();
        result.setPreferenceAnalysis(buildPreferenceAnalysis(context));
        result.setRecommendationSpotList(recommendationSpotList);
        result.setRoutePlan(routePlan);
        result.setExplanationMarkdown(explanationMarkdown);
        result.setWorkflowSummary(Arrays.asList(
                "推荐智能体：结合用户偏好、景点标签和热度筛选候选景点",
                "规划智能体：基于候选景点生成按天路线和预算估算",
                "讲解智能体：把推荐依据和路线安排整理成可读说明"));
        return result;
    }

    private TourismAgentContext buildContext(TourismMultiAgentRequest request) {
        TourismAgentContext context = new TourismAgentContext();
        String userInputText = StringUtils.defaultString(request.getUserInputText());
        context.setUserInputText(userInputText);
        context.setDayCount(resolveDayCount(request.getDayCount(), userInputText));
        context.setBudget(resolveBudget(request.getBudget(), userInputText));
        context.setSpotLocation(resolveLocation(request.getSpotLocation(), userInputText));
        context.setPreferredTagList(resolveTagList(request.getPreferredTagList(), userInputText));
        context.setRecommendSize(request.getRecommendSize() == null ? 6 : request.getRecommendSize());
        context.setPreferenceSummary(buildPreferenceSummary(context));
        return context;
    }

    private Integer resolveDayCount(Integer inputDayCount, String userInputText) {
        if (inputDayCount != null && inputDayCount > 0) {
            return inputDayCount;
        }
        Matcher matcher = DAY_PATTERN.matcher(userInputText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 2;
    }

    private BigDecimal resolveBudget(BigDecimal inputBudget, String userInputText) {
        if (inputBudget != null && inputBudget.compareTo(BigDecimal.ZERO) > 0) {
            return inputBudget;
        }
        Matcher matcher = BUDGET_PATTERN.matcher(userInputText);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        return null;
    }

    private String resolveLocation(String inputLocation, String userInputText) {
        if (StringUtils.isNotBlank(inputLocation)) {
            return inputLocation.trim();
        }
        List<Spot> spotList = spotService.list(new QueryWrapper<Spot>().eq("spotStatus", 1));
        for (Spot spot : spotList) {
            String location = StringUtils.defaultString(spot.getSpotLocation()).trim();
            if (StringUtils.isBlank(location) || location.contains("°")) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(userInputText, location)) {
                return location;
            }
        }
        return "";
    }

    private List<String> resolveTagList(List<String> inputTagList, String userInputText) {
        Set<String> tagSet = new LinkedHashSet<>();
        if (CollUtil.isNotEmpty(inputTagList)) {
            tagSet.addAll(inputTagList.stream()
                    .filter(StringUtils::isNotBlank)
                    .map(String::trim)
                    .collect(Collectors.toList()));
        }

        List<String> databaseTagList = loadAllSpotTags();
        for (String tag : databaseTagList) {
            if (StringUtils.containsIgnoreCase(userInputText, tag)) {
                tagSet.add(tag);
            }
        }
        for (String tag : DEFAULT_TAG_LIST) {
            if (StringUtils.containsIgnoreCase(userInputText, tag)) {
                tagSet.add(tag);
            }
        }
        return new ArrayList<>(tagSet);
    }

    private List<String> loadAllSpotTags() {
        List<Spot> spotList = spotService.list(new QueryWrapper<Spot>().eq("spotStatus", 1));
        Set<String> tagSet = new LinkedHashSet<>();
        for (Spot spot : spotList) {
            String spotTags = spot.getSpotTags();
            if (StringUtils.isBlank(spotTags)) {
                continue;
            }
            try {
                tagSet.addAll(JSONUtil.toList(spotTags, String.class));
            } catch (Exception ignored) {
            }
        }
        return new ArrayList<>(tagSet);
    }

    private String buildPreferenceSummary(TourismAgentContext context) {
        List<String> summaryPartList = new ArrayList<>();
        summaryPartList.add(context.getDayCount() + "天行程");
        if (context.getBudget() != null) {
            summaryPartList.add("预算约" + context.getBudget().stripTrailingZeros().toPlainString() + "元");
        }
        if (StringUtils.isNotBlank(context.getSpotLocation())) {
            summaryPartList.add("目标地区为" + context.getSpotLocation());
        }
        if (CollUtil.isNotEmpty(context.getPreferredTagList())) {
            summaryPartList.add("偏好" + String.join("、", context.getPreferredTagList()));
        }
        if (StringUtils.isNotBlank(context.getUserInputText())) {
            summaryPartList.add("原始需求为：" + context.getUserInputText());
        }
        return String.join("，", summaryPartList);
    }

    private TourismPreferenceVO buildPreferenceAnalysis(TourismAgentContext context) {
        TourismPreferenceVO preferenceVO = new TourismPreferenceVO();
        preferenceVO.setUserInputText(context.getUserInputText());
        preferenceVO.setParsedDayCount(context.getDayCount());
        preferenceVO.setParsedBudget(context.getBudget());
        preferenceVO.setParsedSpotLocation(context.getSpotLocation());
        preferenceVO.setParsedTagList(CollUtil.isEmpty(context.getPreferredTagList()) ? Collections.emptyList() : context.getPreferredTagList());
        preferenceVO.setAnalysisSummary(context.getPreferenceSummary());
        return preferenceVO;
    }
}
