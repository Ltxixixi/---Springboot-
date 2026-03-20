package com.xiaobaitiao.springbootinit.service;

import com.xiaobaitiao.springbootinit.model.dto.tourismAgent.TourismMultiAgentRequest;
import com.xiaobaitiao.springbootinit.model.vo.TourismMultiAgentVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 多智能协作旅游服务
 */
public interface TourismMultiAgentService {

    TourismMultiAgentVO generatePlan(TourismMultiAgentRequest tourismMultiAgentRequest, HttpServletRequest request);
}
