package com.xiaobaitiao.springbootinit.service.impl.agent;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.agent.TourismRecommendAgentService;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐智能体实现
 */
@Service
public class TourismRecommendAgentServiceImpl implements TourismRecommendAgentService {

    @Resource
    private SpotService spotService;

    @Override
    public List<SpotVO> recommend(TourismAgentContext context, HttpServletRequest request) {
        int recommendSize = context.getRecommendSize() == null ? 6 : Math.min(context.getRecommendSize(), 10);
        List<SpotVO> recommendSpotList = new ArrayList<>(spotService.getRecommendSpotVOList(Math.max(recommendSize * 2, 10), request));
        List<SpotVO> filteredRecommendSpotList = recommendSpotList.stream()
                .filter(spotVO -> matchContext(spotVO, context))
                .limit(recommendSize)
                .collect(Collectors.toList());
        if (filteredRecommendSpotList.size() >= recommendSize) {
            return filteredRecommendSpotList;
        }

        List<Spot> openSpotList = spotService.list(new QueryWrapper<Spot>()
                .eq("spotStatus", 1)
                .orderByDesc("viewNum")
                .orderByDesc("favourNum"));
        for (Spot spot : openSpotList) {
            SpotVO spotVO = spotService.getSpotVO(spot, request);
            if (!matchContext(spotVO, context)) {
                continue;
            }
            boolean exists = filteredRecommendSpotList.stream().anyMatch(item -> item.getId().equals(spotVO.getId()));
            if (exists) {
                continue;
            }
            if (StringUtils.isBlank(spotVO.getRecommendReason())) {
                spotVO.setRecommendReason(buildReason(spotVO, context));
            }
            filteredRecommendSpotList.add(spotVO);
            if (filteredRecommendSpotList.size() >= recommendSize) {
                break;
            }
        }
        return filteredRecommendSpotList;
    }

    private boolean matchContext(SpotVO spotVO, TourismAgentContext context) {
        boolean locationMatch = StringUtils.isBlank(context.getSpotLocation())
                || StringUtils.containsIgnoreCase(StringUtils.defaultString(spotVO.getSpotLocation()), context.getSpotLocation());
        boolean tagMatch = CollUtil.isEmpty(context.getPreferredTagList())
                || CollUtil.intersectionDistinct(CollUtil.emptyIfNull(spotVO.getSpotTagList()), context.getPreferredTagList()).size() > 0;
        return locationMatch && tagMatch;
    }

    private String buildReason(SpotVO spotVO, TourismAgentContext context) {
        List<String> matchedTagList = CollUtil.intersectionDistinct(CollUtil.emptyIfNull(spotVO.getSpotTagList()),
                CollUtil.emptyIfNull(context.getPreferredTagList()));
        if (CollUtil.isNotEmpty(matchedTagList)) {
            return "推荐智能体识别到你偏好" + String.join("、", matchedTagList);
        }
        if (StringUtils.isNotBlank(context.getSpotLocation())
                && StringUtils.containsIgnoreCase(StringUtils.defaultString(spotVO.getSpotLocation()), context.getSpotLocation())) {
            return "推荐智能体按目标地区筛选到该景点";
        }
        return "推荐智能体综合热度与评分选出该景点";
    }
}
