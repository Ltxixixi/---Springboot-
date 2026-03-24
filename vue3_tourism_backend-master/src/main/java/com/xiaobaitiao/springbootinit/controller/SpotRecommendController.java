package com.xiaobaitiao.springbootinit.controller;

import com.xiaobaitiao.springbootinit.common.BaseResponse;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.common.ResultUtils;
import com.xiaobaitiao.springbootinit.model.dto.spotRecommend.SpotRecommendRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRecommend.SpotRecommendVO;
import com.xiaobaitiao.springbootinit.service.SpotRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 景点推荐接口
 *
 * 基于协同过滤与内容特征融合的混合推荐算法
 */
@RestController
@RequestMapping("/spot/recommend")
@Slf4j
public class SpotRecommendController {

    @Resource
    private SpotRecommendService spotRecommendService;

    /**
     * 获取景点推荐
     *
     * @param request 推荐请求参数
     * @param httpRequest HTTP请求（用于获取登录用户）
     * @return 推荐结果
     */
    @PostMapping
    public BaseResponse<SpotRecommendVO> getRecommendations(
            @RequestBody(required = false) SpotRecommendRequest request,
            HttpServletRequest httpRequest) {
        if (request == null) {
            request = new SpotRecommendRequest();
        }
        SpotRecommendVO result = spotRecommendService.getRecommendations(request, httpRequest);
        return ResultUtils.success(result);
    }

    /**
     * 获取默认推荐（首页推荐）
     *
     * @param size 推荐数量
     * @param httpRequest HTTP请求
     * @return 推荐结果
     */
    @GetMapping("/default")
    public BaseResponse<SpotRecommendVO> getDefaultRecommendations(
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest httpRequest) {
        SpotRecommendRequest request = new SpotRecommendRequest();
        request.setSize(size);
        SpotRecommendVO result = spotRecommendService.getRecommendations(request, httpRequest);
        return ResultUtils.success(result);
    }

    /**
     * 获取个性化推荐
     *
     * @param city 目标城市
     * @param tags 偏好标签（逗号分隔）
     * @param budget 预算
     * @param crowdType 人群类型
     * @param size 推荐数量
     * @param httpRequest HTTP请求
     * @return 推荐结果
     */
    @GetMapping("/personalized")
    public BaseResponse<SpotRecommendVO> getPersonalizedRecommendations(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) java.math.BigDecimal budget,
            @RequestParam(required = false) String crowdType,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest httpRequest) {

        SpotRecommendRequest request = new SpotRecommendRequest();

        if (city != null && !city.isEmpty()) {
            request.setCity(city);
        }

        if (tags != null && !tags.isEmpty()) {
            request.setPreferredTags(java.util.Arrays.asList(tags.split(",")));
        }

        if (budget != null) {
            request.setBudget(budget);
        }

        if (crowdType != null && !crowdType.isEmpty()) {
            request.setCrowdType(crowdType);
        }

        request.setSize(size);

        SpotRecommendVO result = spotRecommendService.getRecommendations(request, httpRequest);
        return ResultUtils.success(result);
    }

    /**
     * 刷新推荐模型（管理员接口）
     *
     * @return 操作结果
     */
    @PostMapping("/refresh")
    public BaseResponse<Boolean> refreshRecommendModel() {
        spotRecommendService.refreshRecommendModel();
        return ResultUtils.success(true);
    }
}
