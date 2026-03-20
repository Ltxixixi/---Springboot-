package com.xiaobaitiao.springbootinit.service.agent;

import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 推荐智能体
 */
public interface TourismRecommendAgentService {

    List<SpotVO> recommend(TourismAgentContext context, HttpServletRequest request);
}
