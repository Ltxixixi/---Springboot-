package com.xiaobaitiao.springbootinit.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.vo.GeneticRouteResult;
import com.xiaobaitiao.springbootinit.service.RouteOptimizationService;
import com.xiaobaitiao.springbootinit.utils.PositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 基于遗传算法的路线优化服务实现
 *
 * 算法设计：
 * 1. 染色体编码：景点ID序列，如 [3, 1, 5, 2, 4] 表示依次游览景点3->1->5->2->4
 * 2. 适应度函数：fitness = 1 / (totalDistance + timePenalty + experiencePenalty)
 *    - totalDistance: 景点间总距离
 *    - timePenalty: 时间约束惩罚（超出游玩时间）
 *    - experiencePenalty: 体验惩罚（不合理顺序，如上午爬山后下午又爬山）
 * 3. 选择算子：锦标赛选择
 * 4. 交叉算子：顺序交叉(OX - Order Crossover)
 * 5. 变异算子：逆序变异(Swap Mutation)、插入变异(Insertion Mutation)
 * 6. 终止条件：达到最大迭代次数或适应度收敛
 *
 * 论文支撑："基于多约束优化的旅游路线生成算法" + "贪心+遗传旅游推荐"
 */
@Service
@Slf4j
public class RouteOptimizationServiceImpl implements RouteOptimizationService {

    // ==================== 遗传算法参数 ====================
    private static final int DEFAULT_POPULATION_SIZE = 50;
    private static final int DEFAULT_MAX_ITERATIONS = 100;
    private static final double DEFAULT_MUTATION_RATE = 0.15;
    private static final int ELITE_COUNT = 5;  // 精英保留数量
    private static final int TOURNAMENT_SIZE = 3;  // 锦标赛大小

    // ==================== 适应度权重 ====================
    private static final double DISTANCE_WEIGHT = 1.0;      // 距离权重
    private static final double TIME_PENALTY_WEIGHT = 2.0; // 时间惩罚权重
    private static final double EXPERIENCE_PENALTY_WEIGHT = 1.5; // 体验惩罚权重

    // ==================== 默认值 ====================
    private static final int DEFAULT_SPOT_DURATION = 120;  // 默认游玩时长（分钟）
    private static final int DEFAULT_TRANSIT_TIME = 30;    // 默认交通时长（分钟）

    @Override
    public GeneticRouteResult optimizeDailyRoute(List<Spot> spotList, int maxIterations,
                                                int populationSize, double mutationRate) {
        if (CollUtil.isEmpty(spotList)) {
            return new GeneticRouteResult();
        }

        if (spotList.size() == 1) {
            return new GeneticRouteResult(
                    new Long[]{spotList.get(0).getId()},
                    1.0, 0, 0, 0, 0
            );
        }

        long startTime = System.currentTimeMillis();

        // 构建景点映射
        Map<Long, Spot> spotMap = spotList.stream()
                .collect(Collectors.toMap(Spot::getId, s -> s, (a, b) -> a));

        // 初始化种群
        List<Long[]> population = initializePopulation(spotList, populationSize);

        // 计算初始适应度
        List<Double> fitnessScores = new ArrayList<>();
        for (Long[] chromosome : population) {
            fitnessScores.add(calculateFitness(chromosome, spotMap));
        }

        // 遗传算法迭代
        int iterations = 0;
        double bestFitness = 0;
        Long[] bestChromosome = null;

        for (int iter = 0; iter < maxIterations; iter++) {
            iterations++;

            // 评估适应度
            fitnessScores.clear();
            for (Long[] chromosome : population) {
                fitnessScores.add(calculateFitness(chromosome, spotMap));
            }

            // 记录最佳个体
            int bestIdx = 0;
            for (int i = 1; i < fitnessScores.size(); i++) {
                if (fitnessScores.get(i) > fitnessScores.get(bestIdx)) {
                    bestIdx = i;
                }
            }

            if (bestFitness < fitnessScores.get(bestIdx)) {
                bestFitness = fitnessScores.get(bestIdx);
                bestChromosome = population.get(bestIdx).clone();
            }

            // 创建新一代
            List<Long[]> newPopulation = new ArrayList<>();

            // 精英保留
            List<Long[]> elite = selectElite(population, fitnessScores, ELITE_COUNT);
            newPopulation.addAll(elite);

            // 生成新个体
            while (newPopulation.size() < populationSize) {
                // 锦标赛选择父代
                Long[] parent1 = tournamentSelect(population, fitnessScores);
                Long[] parent2 = tournamentSelect(population, fitnessScores);

                // 交叉
                Long[] child = orderCrossover(parent1, parent2);

                // 变异
                if (ThreadLocalRandom.current().nextDouble() < mutationRate) {
                    child = mutate(child, spotList.size());
                }

                newPopulation.add(child);
            }

            population = newPopulation;
        }

        // 最终评估
        fitnessScores.clear();
        for (Long[] chromosome : population) {
            fitnessScores.add(calculateFitness(chromosome, spotMap));
        }

        int finalBestIdx = 0;
        for (int i = 1; i < fitnessScores.size(); i++) {
            if (fitnessScores.get(i) > fitnessScores.get(finalBestIdx)) {
                finalBestIdx = i;
            }
        }

        if (bestChromosome == null || fitnessScores.get(finalBestIdx) > bestFitness) {
            bestChromosome = population.get(finalBestIdx).clone();
            bestFitness = fitnessScores.get(finalBestIdx);
        }

        long elapsedMs = System.currentTimeMillis() - startTime;

        // 计算最终距离和时长
        double totalDistance = calculateTotalDistance(bestChromosome, spotMap);
        int totalDuration = calculateTotalDuration(bestChromosome, spotMap);

        GeneticRouteResult result = new GeneticRouteResult(
                bestChromosome, bestFitness, totalDistance, totalDuration, elapsedMs, iterations
        );

        result.setOptimizationNote(generateOptimizationNote(bestChromosome, spotMap, bestFitness));

        log.info("遗传算法优化完成，迭代{}次，耗时{}ms，最优适应度{:.4f}",
                iterations, elapsedMs, bestFitness);

        return result;
    }

    @Override
    public List<GeneticRouteResult> optimizeMultiDayRoute(List<List<Spot>> dailySpots, int maxIterations,
                                                          int populationSize, double mutationRate) {
        if (CollUtil.isEmpty(dailySpots)) {
            return Collections.emptyList();
        }

        List<GeneticRouteResult> results = new ArrayList<>();

        for (int day = 0; day < dailySpots.size(); day++) {
            List<Spot> daySpots = dailySpots.get(day);
            GeneticRouteResult dayResult = optimizeDailyRoute(daySpots, maxIterations, populationSize, mutationRate);
            results.add(dayResult);
        }

        return results;
    }

    @Override
    public List<Long> greedyRoute(List<Spot> spotList) {
        if (CollUtil.isEmpty(spotList)) {
            return Collections.emptyList();
        }

        if (spotList.size() == 1) {
            return Collections.singletonList(spotList.get(0).getId());
        }

        List<Spot> remaining = new ArrayList<>(spotList);
        List<Long> route = new ArrayList<>();

        // 从第一个景点开始
        Spot current = remaining.remove(0);
        route.add(current.getId());

        // 贪心选择下一个最近的景点
        while (!remaining.isEmpty()) {
            Spot nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (Spot candidate : remaining) {
                double distance = calculateSpotDistance(current, candidate);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = candidate;
                }
            }

            if (nearest != null) {
                route.add(nearest.getId());
                remaining.remove(nearest);
                current = nearest;
            }
        }

        return route;
    }

    @Override
    public double calculateFitness(Long[] chromosome, Map<Long, Spot> spotMap) {
        if (chromosome == null || chromosome.length == 0) {
            return 0;
        }

        if (chromosome.length == 1) {
            return 1.0; // 单景点适应度为1
        }

        // 计算总距离
        double totalDistance = calculateTotalDistance(chromosome, spotMap);

        // 计算体验惩罚
        double experiencePenalty = calculateExperiencePenalty(chromosome, spotMap);

        // 计算时间惩罚
        double timePenalty = calculateTimePenalty(chromosome, spotMap);

        // 适应度 = 1 / (总成本)，成本越低适应度越高
        double cost = totalDistance * DISTANCE_WEIGHT
                + timePenalty * TIME_PENALTY_WEIGHT
                + experiencePenalty * EXPERIENCE_PENALTY_WEIGHT;

        return cost > 0 ? 1.0 / cost : 1.0;
    }

    /**
     * 初始化种群
     */
    private List<Long[]> initializePopulation(List<Spot> spotList, int populationSize) {
        List<Long[]> population = new ArrayList<>();
        List<Long> spotIds = spotList.stream().map(Spot::getId).collect(Collectors.toList());

        // 添加一个基于贪心的个体
        population.add(greedyRoute(spotList).toArray(new Long[0]));

        // 随机生成剩余个体
        for (int i = 1; i < populationSize; i++) {
            List<Long> shuffled = new ArrayList<>(spotIds);
            Collections.shuffle(shuffled, ThreadLocalRandom.current());
            population.add(shuffled.toArray(new Long[0]));
        }

        return population;
    }

    /**
     * 精英选择
     */
    private List<Long[]> selectElite(List<Long[]> population, List<Double> fitnessScores, int count) {
        List<Long[]> elite = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < fitnessScores.size(); i++) {
            indices.add(i);
        }

        // 按适应度排序，取前count个
        indices.sort((a, b) -> Double.compare(fitnessScores.get(b), fitnessScores.get(a)));

        for (int i = 0; i < Math.min(count, indices.size()); i++) {
            elite.add(population.get(indices.get(i)).clone());
        }

        return elite;
    }

    /**
     * 锦标赛选择
     */
    private Long[] tournamentSelect(List<Long[]> population, List<Double> fitnessScores) {
        int bestIdx = ThreadLocalRandom.current().nextInt(population.size());

        for (int i = 1; i < TOURNAMENT_SIZE; i++) {
            int idx = ThreadLocalRandom.current().nextInt(population.size());
            if (fitnessScores.get(idx) > fitnessScores.get(bestIdx)) {
                bestIdx = idx;
            }
        }

        return population.get(bestIdx);
    }

    /**
     * 顺序交叉 (Order Crossover - OX)
     */
    private Long[] orderCrossover(Long[] parent1, Long[] parent2) {
        int length = parent1.length;
        Long[] child = new Long[length];
        Arrays.fill(child, null);

        // 随机选择两个交叉点
        int start = ThreadLocalRandom.current().nextInt(length);
        int end = start + ThreadLocalRandom.current().nextInt(length - start) + 1;

        // 复制父代1的片段到子代
        Set<Long> copied = new HashSet<>();
        for (int i = start; i < end; i++) {
            child[i] = parent1[i];
            copied.add(parent1[i]);
        }

        // 从父代2按顺序复制剩余基因
        int pos = end % length;
        for (int i = 0; i < length && pos != start; i++) {
            int idx = (end + i) % length;
            Long gene = parent2[idx];
            if (!copied.contains(gene)) {
                child[pos] = gene;
                copied.add(gene);
                pos = (pos + 1) % length;
            }
        }

        return child;
    }

    /**
     * 变异操作（逆序变异 + 插入变异）
     */
    private Long[] mutate(Long[] chromosome, int length) {
        if (chromosome.length < 2) {
            return chromosome;
        }

        // 50%概率逆序变异，50%概率插入变异
        if (ThreadLocalRandom.current().nextBoolean()) {
            // 逆序变异：随机选择两个位置，交换它们之间的所有基因
            int pos1 = ThreadLocalRandom.current().nextInt(length);
            int pos2 = ThreadLocalRandom.current().nextInt(length);

            int start = Math.min(pos1, pos2);
            int end = Math.max(pos1, pos2);

            while (start < end) {
                Long temp = chromosome[start];
                chromosome[start] = chromosome[end];
                chromosome[end] = temp;
                start++;
                end--;
            }
        } else {
            // 插入变异：随机选择一个基因，将其插入到另一个随机位置
            int from = ThreadLocalRandom.current().nextInt(length);
            int to = ThreadLocalRandom.current().nextInt(length);

            if (from != to) {
                Long gene = chromosome[from];
                List<Long> list = new ArrayList<>(Arrays.asList(chromosome));
                list.remove(from);
                list.add(to, gene);
                return list.toArray(chromosome);
            }
        }

        return chromosome;
    }

    /**
     * 计算总距离
     */
    private double calculateTotalDistance(Long[] chromosome, Map<Long, Spot> spotMap) {
        if (chromosome == null || chromosome.length < 2) {
            return 0;
        }

        double total = 0;
        for (int i = 0; i < chromosome.length - 1; i++) {
            Spot from = spotMap.get(chromosome[i]);
            Spot to = spotMap.get(chromosome[i + 1]);
            if (from != null && to != null) {
                total += calculateSpotDistance(from, to);
            }
        }

        return total;
    }

    /**
     * 计算两景点间的距离
     */
    private double calculateSpotDistance(Spot from, Spot to) {
        if (from == null || to == null) {
            return DEFAULT_TRANSIT_TIME; // 默认交通时间作为距离
        }

        Double lat1 = from.getLatitude() != null ? from.getLatitude().doubleValue() : null;
        Double lon1 = from.getLongitude() != null ? from.getLongitude().doubleValue() : null;
        Double lat2 = to.getLatitude() != null ? to.getLatitude().doubleValue() : null;
        Double lon2 = to.getLongitude() != null ? to.getLongitude().doubleValue() : null;

        if (lat1 != null && lon1 != null && lat2 != null && lon2 != null) {
            // 返回距离（公里）
            return PositionUtil.getDistance(lon1, lat1, lon2, lat2);
        }

        return DEFAULT_TRANSIT_TIME; // 无坐标时返回默认距离
    }

    /**
     * 计算总游玩时长
     */
    private int calculateTotalDuration(Long[] chromosome, Map<Long, Spot> spotMap) {
        if (chromosome == null || chromosome.length == 0) {
            return 0;
        }

        int total = 0;
        for (Long spotId : chromosome) {
            Spot spot = spotMap.get(spotId);
            if (spot != null && spot.getVisitDurationMinutes() != null) {
                total += spot.getVisitDurationMinutes();
            } else {
                total += DEFAULT_SPOT_DURATION;
            }
        }

        // 加上交通时间
        total += (chromosome.length - 1) * DEFAULT_TRANSIT_TIME;

        return total;
    }

    /**
     * 计算体验惩罚
     * 惩罚不合理的游玩顺序
     */
    private double calculateExperiencePenalty(Long[] chromosome, Map<Long, Spot> spotMap) {
        if (chromosome == null || chromosome.length < 2) {
            return 0;
        }

        double penalty = 0;

        for (int i = 0; i < chromosome.length - 1; i++) {
            Spot current = spotMap.get(chromosome[i]);
            Spot next = spotMap.get(chromosome[i + 1]);

            if (current == null || next == null) continue;

            // 惩罚：连续游览同类型景点
            String currentType = getSpotType(current);
            String nextType = getSpotType(next);

            if (currentType.equals(nextType)) {
                penalty += 5; // 同类型景点连续游览增加惩罚
            }

            // 惩罚：下午游览博物馆/纪念馆（适合下午）
            if (i > 0 && isAfternoonSpot(next) && i < chromosome.length - 1) {
                penalty += 2;
            }
        }

        return penalty;
    }

    /**
     * 获取景点类型
     */
    private String getSpotType(Spot spot) {
        if (spot == null || spot.getSpotTags() == null) {
            return "general";
        }

        String tags = spot.getSpotTags().toLowerCase();
        if (tags.contains("博物馆") || tags.contains("纪念馆")) {
            return "museum";
        } else if (tags.contains("山") || tags.contains("徒步")) {
            return "mountain";
        } else if (tags.contains("夜") || tags.contains("夜景")) {
            return "night";
        } else if (tags.contains("寺庙") || tags.contains("宗教")) {
            return "temple";
        } else if (tags.contains("古镇") || tags.contains("古街")) {
            return "ancient";
        }

        return "general";
    }

    /**
     * 判断是否为适合下午游览的景点
     */
    private boolean isAfternoonSpot(Spot spot) {
        if (spot == null || spot.getSpotTags() == null) {
            return false;
        }

        String tags = spot.getSpotTags();
        return tags.contains("博物馆") || tags.contains("纪念馆") ||
               tags.contains("展览馆") || tags.contains("室内");
    }

    /**
     * 计算时间惩罚
     */
    private double calculateTimePenalty(Long[] chromosome, Map<Long, Spot> spotMap) {
        // 简化版本：主要考虑总时长是否合理
        // 这里可以扩展加入开放时间、游玩时段等约束
        return 0;
    }

    /**
     * 生成优化说明
     */
    private String generateOptimizationNote(Long[] chromosome, Map<Long, Spot> spotMap, double fitness) {
        double totalDistance = calculateTotalDistance(chromosome, spotMap);
        int totalDuration = calculateTotalDuration(chromosome, spotMap);

        return String.format(
                "已通过遗传算法优化路线顺序，总距离约%.1f公里，预计游玩时长约%d分钟，适应度得分%.4f",
                totalDistance, totalDuration, fitness
        );
    }
}
