package com.xiaobaitiao.springbootinit.service.impl.agent;

import cn.hutool.core.collection.CollUtil;
import com.xiaobaitiao.springbootinit.model.dto.spotRoute.SpotRoutePlanRequest;
import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.SpotRouteService;
import com.xiaobaitiao.springbootinit.service.agent.TourismPlanningAgentService;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规划智能体实现
 */
@Service
public class TourismPlanningAgentServiceImpl implements TourismPlanningAgentService {

    @Resource
    private SpotRouteService spotRouteService;

    @Override
    public SpotRoutePlanVO plan(TourismAgentContext context, List<SpotVO> recommendationSpotList, HttpServletRequest request) {
        SpotRoutePlanRequest planRequest = new SpotRoutePlanRequest();
        planRequest.setDayCount(context.getDayCount());
        planRequest.setBudget(context.getBudget());
        planRequest.setSpotLocation(context.getSpotLocation());
        planRequest.setSpotTagList(context.getPreferredTagList());
        if (CollUtil.isNotEmpty(recommendationSpotList)) {
            planRequest.setCandidateSpotIdList(recommendationSpotList.stream()
                    .map(SpotVO::getId)
                    .collect(Collectors.toList()));
        }
        return spotRouteService.generateRoutePlan(planRequest, request);
    }
}
