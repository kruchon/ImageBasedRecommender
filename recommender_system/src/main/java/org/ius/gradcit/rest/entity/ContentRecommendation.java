package org.ius.gradcit.rest.entity;

public class ContentRecommendation {

    private String imgId;
    /*private String[] path;
    private Double interestWeight;
    private Double pathWeight;
    private Double probability;*/
    private Double weight;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
/*

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public Double getInterestWeight() {
        return interestWeight;
    }

    public void setInterestWeight(Double interestWeight) {
        this.interestWeight = interestWeight;
    }

    public Double getPathWeight() {
        return pathWeight;
    }

    public void setPathWeight(Double pathWeight) {
        this.pathWeight = pathWeight;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
*/

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
