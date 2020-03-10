package org.ius.gradcit.rest.controller;

import org.ius.gradcit.logic.recommender.Recommender;
import org.ius.gradcit.rest.entity.ContentRecommendation;
import org.ius.gradcit.rest.entity.RecommendationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController {

    private final Recommender recommender;

    @Autowired
    public RecommendationController(Recommender recommender) {
        this.recommender = recommender;
    }

    @PostMapping("getContentRecommendation")
    @ResponseBody
    public List<ContentRecommendation> getContentRecommendation(@RequestBody RecommendationRequest recommendationRequest) {
        String userId = recommendationRequest.getUserId();
        return recommender.generateContentRecommendation(userId);
    }

}
