package com.xiaobaitiao.springbootinit.service.agent;

import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 规划智能体
 */
public interface TourismPlanningAgentService {

    SpotRoutePlanVO plan(TourismAgentContext context, List<SpotVO> recommendationSpotList, HttpServletRequest request);
}
