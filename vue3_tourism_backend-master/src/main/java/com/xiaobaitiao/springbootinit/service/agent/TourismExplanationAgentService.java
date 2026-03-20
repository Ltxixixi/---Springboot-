package com.xiaobaitiao.springbootinit.service.agent;

import com.xiaobaitiao.springbootinit.model.vo.SpotRoutePlanVO;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;

import java.util.List;

/**
 * 讲解智能体
 */
public interface TourismExplanationAgentService {

    String explain(TourismAgentContext context, List<SpotVO> recommendationSpotList, SpotRoutePlanVO routePlan);
}
