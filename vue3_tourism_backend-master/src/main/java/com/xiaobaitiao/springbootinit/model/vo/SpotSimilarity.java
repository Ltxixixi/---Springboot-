package com.xiaobaitiao.springbootinit.model.vo;

/**
 * 景点相似度实体
 */
public class SpotSimilarity implements Comparable<SpotSimilarity> {

    private Long spotId;
    private Double similarity;

    public SpotSimilarity() {
    }

    public SpotSimilarity(Long spotId, Double similarity) {
        this.spotId = spotId;
        this.similarity = similarity;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @Override
    public int compareTo(SpotSimilarity o) {
        return Double.compare(o.getSimilarity(), this.similarity);
    }
}
