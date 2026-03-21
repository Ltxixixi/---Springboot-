package com.xiaobaitiao.springbootinit.service.impl.agent;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.SpotVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.agent.TourismRecommendAgentService;
import com.xiaobaitiao.springbootinit.service.agent.model.TourismAgentContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
        List<SpotVO> baseRecommendSpotList = new ArrayList<>(spotService.getRecommendSpotVOList(Math.max(recommendSize * 3, 12), request));
        Set<Long> baseRecommendSpotIdSet = baseRecommendSpotList.stream()
                .map(SpotVO::getId)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Spot> openSpotList = spotService.list(new QueryWrapper<Spot>()
                .eq("spotStatus", 1)
                .orderByDesc("viewNum")
                .orderByDesc("favourNum"));
        if (CollUtil.isEmpty(openSpotList)) {
            return new ArrayList<>();
        }

        double maxViewNum = openSpotList.stream()
                .map(Spot::getViewNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);
        double maxFavourNum = openSpotList.stream()
                .map(Spot::getFavourNum)
                .filter(ObjectUtils::isNotEmpty)
                .mapToDouble(Integer::doubleValue)
                .max()
                .orElse(1D);

        List<RecommendCandidate> candidateList = new ArrayList<>();
        for (Spot spot : openSpotList) {
            SpotVO spotVO = spotService.getSpotVO(spot, request);
            candidateList.add(buildCandidate(spotVO, context, baseRecommendSpotIdSet, maxViewNum, maxFavourNum));
        }
        return candidateList.stream()
                .sorted(Comparator.comparingDouble(RecommendCandidate::getScore).reversed())
                .limit(recommendSize)
                .map(candidate -> {
                    SpotVO spotVO = candidate.getSpotVO();
                    spotVO.setRecommendReason(candidate.getRecommendReason());
                    return spotVO;
                })
                .collect(Collectors.toList());
    }

    private RecommendCandidate buildCandidate(SpotVO spotVO,
                                              TourismAgentContext context,
                                              Set<Long> baseRecommendSpotIdSet,
                                              double maxViewNum,
                                              double maxFavourNum) {
        List<String> reasonList = new ArrayList<>();
        double score = 0D;

        Set<String> matchedTagSet = CollUtil.intersectionDistinct(CollUtil.emptyIfNull(spotVO.getSpotTagList()),
                CollUtil.emptyIfNull(context.getPreferredTagList()));
        List<String> matchedTagList = new ArrayList<>(matchedTagSet);
        if (CollUtil.isNotEmpty(matchedTagList)) {
            score += matchedTagList.size() * 18D;
            reasonList.add("匹配偏好标签：" + String.join("、", matchedTagList));
        } else if (CollUtil.isNotEmpty(context.getPreferredTagList())) {
            score -= 4D;
        }

        boolean locationMatched = false;
        if (StringUtils.isNotBlank(context.getSpotLocation())) {
            String spotLocation = StringUtils.defaultString(spotVO.getSpotLocation());
            locationMatched = StringUtils.containsIgnoreCase(spotLocation, context.getSpotLocation())
                    || StringUtils.containsIgnoreCase(context.getSpotLocation(), spotLocation);
            if (locationMatched) {
                score += 24D;
                reasonList.add("地区匹配：" + context.getSpotLocation());
            } else {
                score -= 6D;
            }
        }

        List<String> matchedKeywordList = findMatchedKeywordList(spotVO, context);
        if (CollUtil.isNotEmpty(matchedKeywordList)) {
            score += Math.min(14D, matchedKeywordList.size() * 5D);
            reasonList.add("贴合输入关键词：" + String.join("、", matchedKeywordList));
        }

        double hotScore = calculateHotScore(spotVO, maxViewNum, maxFavourNum);
        score += hotScore;
        if (hotScore >= 7D) {
            reasonList.add("热度表现较好");
        }

        if (baseRecommendSpotIdSet.contains(spotVO.getId())) {
            score += 8D;
            reasonList.add("基础推荐模型命中");
        }

        if (CollUtil.isEmpty(reasonList)) {
            reasonList.add(locationMatched ? "综合地区热度推荐" : "综合热度与标签相关性推荐");
        }
        return new RecommendCandidate(spotVO, score, String.join("；", reasonList));
    }

    private List<String> findMatchedKeywordList(SpotVO spotVO, TourismAgentContext context) {
        String userInputText = StringUtils.defaultString(context.getUserInputText());
        if (StringUtils.isBlank(userInputText)) {
            return new ArrayList<>();
        }
        String searchableText = String.join(" ",
                StringUtils.defaultString(spotVO.getSpotName()),
                StringUtils.defaultString(spotVO.getSpotDescription()),
                String.join(" ", CollUtil.emptyIfNull(spotVO.getSpotTagList())));
        String[] tokenArray = userInputText.split("[,，。；;、\\s]+");
        List<String> matchedKeywordList = new ArrayList<>();
        for (String token : tokenArray) {
            String keyword = StringUtils.trimToEmpty(token);
            if (keyword.length() < 2 || StringUtils.isNumeric(keyword)) {
                continue;
            }
            if (StringUtils.isNotBlank(context.getSpotLocation()) && StringUtils.equalsIgnoreCase(keyword, context.getSpotLocation())) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(searchableText, keyword) && !matchedKeywordList.contains(keyword)) {
                matchedKeywordList.add(keyword);
            }
            if (matchedKeywordList.size() >= 3) {
                break;
            }
        }
        return matchedKeywordList;
    }

    private double calculateHotScore(SpotVO spotVO, double maxViewNum, double maxFavourNum) {
        double normalizedViewScore = ObjectUtils.isEmpty(spotVO.getViewNum()) ? 0D : (spotVO.getViewNum() / maxViewNum) * 6D;
        double normalizedFavourScore = ObjectUtils.isEmpty(spotVO.getFavourNum()) ? 0D : (spotVO.getFavourNum() / maxFavourNum) * 6D;
        return normalizedViewScore + normalizedFavourScore;
    }

    private static class RecommendCandidate {
        private final SpotVO spotVO;
        private final double score;
        private final String recommendReason;

        private RecommendCandidate(SpotVO spotVO, double score, String recommendReason) {
            this.spotVO = spotVO;
            this.score = score;
            this.recommendReason = recommendReason;
        }

        public SpotVO getSpotVO() {
            return spotVO;
        }

        public double getScore() {
            return score;
        }

        public String getRecommendReason() {
            return recommendReason;
        }
    }
}
