package com.xiaobaitiao.springbootinit.service.impl.agent;

import cn.hutool.core.collection.CollUtil;
import com.xiaobaitiao.springbootinit.manager.TourismAiClient;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanDayVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.agent.TourismExplanationAgentService;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 讲解智能体实现
 */
@Service
public class TourismExplanationAgentServiceImpl implements TourismExplanationAgentService {

    @Resource
    private TourismAiClient tourismAiClient;

    @Override
    public String explain(TourismAgentContext context, List<SpotVO> recommendationSpotList, SpotRoutePlanVO routePlan) {
        if (tourismAiClient.isAvailable()) {
            try {
                return tourismAiClient.chat(buildUserPrompt(context, recommendationSpotList, routePlan));
            } catch (Exception ignored) {
            }
        }
        return buildFallbackExplanation(context, recommendationSpotList, routePlan);
    }

    private String buildUserPrompt(TourismAgentContext context, List<SpotVO> recommendationSpotList, SpotRoutePlanVO routePlan) {
        StringBuilder builder = new StringBuilder();
        builder.append("请把下面的旅游推荐与路线结果整理成 Markdown，结构包含：需求理解、推荐理由、路线安排、出行建议。\n");
        builder.append("用户需求：").append(context.getPreferenceSummary()).append("\n");
        builder.append("推荐景点：\n");
        if (CollUtil.isEmpty(recommendationSpotList)) {
            builder.append("- 暂无明确推荐景点\n");
        } else {
            for (SpotVO spotVO : recommendationSpotList) {
                builder.append("- ")
                        .append(spotVO.getSpotName())
                        .append("：")
                        .append(StringUtils.defaultIfBlank(spotVO.getRecommendReason(), "综合推荐"))
                        .append("\n");
            }
        }
        builder.append("路线结果：\n");
        builder.append("- 总景点数：").append(routePlan.getTotalSpotCount()).append("\n");
        builder.append("- 预估总花费：").append(routePlan.getTotalEstimatedCost()).append(" 元\n");
        if (CollUtil.isNotEmpty(routePlan.getSpotNameList())) {
            builder.append("- 总体路线：").append(String.join(" -> ", routePlan.getSpotNameList())).append("\n");
        }
        if (CollUtil.isNotEmpty(routePlan.getDayPlanList())) {
            for (SpotRoutePlanDayVO dayPlanVO : routePlan.getDayPlanList()) {
                builder.append("- 第")
                        .append(dayPlanVO.getDayNumber())
                        .append("天：")
                        .append(String.join(" -> ", dayPlanVO.getSpotNameList()))
                        .append("，预估花费 ")
                        .append(dayPlanVO.getEstimatedCost())
                        .append(" 元，里程约 ")
                        .append(String.format("%.2f", dayPlanVO.getTotalDistance()))
                        .append(" 公里\n");
            }
        }
        return builder.toString();
    }

    private String buildFallbackExplanation(TourismAgentContext context, List<SpotVO> recommendationSpotList, SpotRoutePlanVO routePlan) {
        StringBuilder builder = new StringBuilder();
        builder.append("## 多智能协作结果\n\n");
        builder.append("### 需求理解\n");
        builder.append("- 游玩天数：").append(context.getDayCount()).append(" 天\n");
        if (context.getBudget() != null) {
            builder.append("- 预算：").append(context.getBudget().stripTrailingZeros().toPlainString()).append(" 元\n");
        }
        if (StringUtils.isNotBlank(context.getSpotLocation())) {
            builder.append("- 目标地区：").append(context.getSpotLocation()).append("\n");
        }
        if (CollUtil.isNotEmpty(context.getPreferredTagList())) {
            builder.append("- 偏好标签：").append(String.join("、", context.getPreferredTagList())).append("\n");
        }
        builder.append("\n### 推荐智能体结论\n");
        if (CollUtil.isEmpty(recommendationSpotList)) {
            builder.append("- 暂未筛出明确推荐景点，已转为综合热度推荐。\n");
        } else {
            for (SpotVO spotVO : recommendationSpotList) {
                builder.append("- ")
                        .append(spotVO.getSpotName())
                        .append("：")
                        .append(StringUtils.defaultIfBlank(spotVO.getRecommendReason(), "综合推荐"))
                        .append("\n");
            }
        }
        builder.append("\n### 规划智能体结论\n");
        builder.append("- 总景点数：").append(routePlan.getTotalSpotCount()).append("\n");
        builder.append("- 预估总花费：").append(routePlan.getTotalEstimatedCost()).append(" 元\n");
        if (CollUtil.isNotEmpty(routePlan.getSpotNameList())) {
            builder.append("- 总体路线：").append(String.join(" -> ", routePlan.getSpotNameList())).append("\n");
        }
        if (CollUtil.isNotEmpty(routePlan.getDayPlanList())) {
            builder.append("\n### 每日安排\n");
            for (SpotRoutePlanDayVO dayPlanVO : routePlan.getDayPlanList()) {
                builder.append("- 第")
                        .append(dayPlanVO.getDayNumber())
                        .append("天：")
                        .append(String.join(" -> ", dayPlanVO.getSpotNameList()))
                        .append("，预估花费 ")
                        .append(dayPlanVO.getEstimatedCost())
                        .append(" 元，里程约 ")
                        .append(String.format("%.2f", dayPlanVO.getTotalDistance()))
                        .append(" 公里\n");
            }
        }
        builder.append("\n### 讲解智能体建议\n");
        builder.append("- 优先按照每天的推荐顺序游玩，能减少路线跳转。\n");
        builder.append("- 如果当天时间不足，可优先保留评分高、标签匹配度高的景点。\n");
        builder.append("- 出发前建议再核对门票、开放时间和天气情况。\n");
        return builder.toString();
    }
}
