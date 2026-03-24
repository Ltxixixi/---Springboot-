package com.xiaobaitiao.springbootinit.model.vo;

/**
 * 遗传算法路线规划结果
 */
public class GeneticRouteResult {

    /**
     * 优化后的路线顺序
     */
    private Long[] routeOrder;

    /**
     * 适应度得分
     */
    private double fitness;

    /**
     * 总距离（公里）
     */
    private double totalDistance;

    /**
     * 总游玩时长（分钟）
     */
    private int totalDuration;

    /**
     * 算法耗时（毫秒）
     */
    private long elapsedMs;

    /**
     * 迭代次数
     */
    private int iterations;

    /**
     * 优化说明
     */
    private String optimizationNote;

    public GeneticRouteResult() {
    }

    public GeneticRouteResult(Long[] routeOrder, double fitness, double totalDistance,
                              int totalDuration, long elapsedMs, int iterations) {
        this.routeOrder = routeOrder;
        this.fitness = fitness;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.elapsedMs = elapsedMs;
        this.iterations = iterations;
    }

    public Long[] getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(Long[] routeOrder) {
        this.routeOrder = routeOrder;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public long getElapsedMs() {
        return elapsedMs;
    }

    public void setElapsedMs(long elapsedMs) {
        this.elapsedMs = elapsedMs;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getOptimizationNote() {
        return optimizationNote;
    }

    public void setOptimizationNote(String optimizationNote) {
        this.optimizationNote = optimizationNote;
    }
}
