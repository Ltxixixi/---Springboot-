package com.xiaobaitiao.springbootinit.service;

import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.GeneticRouteResult;

import java.util.List;
import java.util.Map;

/**
 * 路线优化服务接口
 *
 * 基于遗传算法的路线优化
 * - 染色体编码：景点顺序序列
 * - 适应度函数：总距离 + 时间惩罚 + 体验分
 * - 交叉算子：顺序交叉(OX)
 * - 变异算子：逆序变异、插入变异
 */
public interface RouteOptimizationService {

    /**
     * 使用遗传算法优化单日路线
     *
     * @param spotList 景点列表（顺序代表初始路线）
     * @param maxIterations 最大迭代次数
     * @param populationSize 种群大小
     * @param mutationRate 变异率
     * @return 优化结果
     */
    GeneticRouteResult optimizeDailyRoute(List<Spot> spotList, int maxIterations,
                                         int populationSize, double mutationRate);

    /**
     * 使用遗传算法优化多日路线
     *
     * @param dailySpots 每天的景点列表
     * @param maxIterations 最大迭代次数
     * @param populationSize 种群大小
     * @param mutationRate 变异率
     * @return 每天的优化结果
     */
    List<GeneticRouteResult> optimizeMultiDayRoute(List<List<Spot>> dailySpots, int maxIterations,
                                                   int populationSize, double mutationRate);

    /**
     * 使用贪心算法快速生成路线（适合实时响应）
     *
     * @param spotList 景点列表
     * @return 贪心排序后的景点ID顺序
     */
    List<Long> greedyRoute(List<Spot> spotList);

    /**
     * 计算路线适应度
     *
     * @param chromosome 染色体（景点ID序列）
     * @param spotMap 景点ID到景点的映射
     * @return 适应度得分
     */
    double calculateFitness(Long[] chromosome, Map<Long, Spot> spotMap);
}
