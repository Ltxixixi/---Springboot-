package com.xiaobaitiao.springbootinit.controller;

import com.xiaobaitiao.springbootinit.common.BaseResponse;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.common.ResultUtils;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.model.dto.tourismAgent.TourismMultiAgentRequest;
import com.xiaobaitiao.springbootinit.model.vo.TourismMultiAgentVO;
import com.xiaobaitiao.springbootinit.service.TourismMultiAgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 多智能协作旅游接口
 */
@RestController
@RequestMapping("/tourismAgent")
public class TourismAgentController {

    @Resource
    private TourismMultiAgentService tourismMultiAgentService;

    @PostMapping("/plan")
    public BaseResponse<TourismMultiAgentVO> generatePlan(@RequestBody TourismMultiAgentRequest tourismMultiAgentRequest,
                                                          HttpServletRequest request) {
        ThrowUtils.throwIf(tourismMultiAgentRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(tourismMultiAgentService.generatePlan(tourismMultiAgentRequest, request));
    }
}
