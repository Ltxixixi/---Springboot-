package com.xiaobaitiao.springbootinit.service;

import com.xiaobaitiao.springbootinit.model.dto.spotRecommend.SpotRecommendRequest;
import com.xiaobaitiao.springbootinit.model.dto.spotRecommend.SpotRecommendVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 景点推荐服务接口
 *
 * 采用混合推荐策略：
 * 1. 召回层：协同过滤召回 + 规则召回
 * 2. 排序层：多特征加权排序
 * 3. 重排层：多样性控制
 */
public interface SpotRecommendService {

    /**
     * 获取景点推荐
     *
     * @param request 推荐请求参数
     * @param httpRequest HTTP请求（用于获取登录用户）
     * @return 推荐结果
     */
    SpotRecommendVO getRecommendations(SpotRecommendRequest request, HttpServletRequest httpRequest);

    /**
     * 刷新推荐模型
     */
    void refreshRecommendModel();
}
