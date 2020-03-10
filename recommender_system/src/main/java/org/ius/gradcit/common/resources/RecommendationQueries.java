package org.ius.gradcit.common.resources;

import org.ius.gradcit.common.resources.ResourceReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendationQueries {

    @Bean
    public String contentRecommendationQuery() {
        return ResourceReader.readFileToString("queries/content_recommendation.cypher");
    }

}
