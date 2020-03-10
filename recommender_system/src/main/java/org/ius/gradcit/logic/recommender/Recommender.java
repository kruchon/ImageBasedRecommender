package org.ius.gradcit.logic.recommender;

import org.ius.gradcit.rest.entity.ContentRecommendation;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Recommender {

    private final SessionFactory sessionFactory;

    private static final String USER_ID_PARAMETER = "userId";

    @Value("${gradcit-recommender.search_depth}")
    private String searchDepth;

    @Autowired
    @Qualifier("contentRecommendationQuery")
    private String contentRecommendationQuery;

    @Autowired
    public Recommender(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ContentRecommendation> generateContentRecommendation(String userExternalId) {
        return generateContentRecommendation(0.7f, userExternalId, searchDepth);
    }

    private LinkedList<ContentRecommendation> mapResult(Result result) {
        Iterator<Map<String, Object>> resultIterator = result.iterator();
        LinkedList<ContentRecommendation> contentRecommendations = new LinkedList<>();
        while (resultIterator.hasNext()) {
            Map<String, Object> resultRow = resultIterator.next();
            contentRecommendations.add(mapRow(resultRow));
        }
        return contentRecommendations;
    }

    private ContentRecommendation mapRow(Map<String, Object> resultRow) {
        ContentRecommendation contentRecommendation = new ContentRecommendation();
        contentRecommendation.setImgId((String) resultRow.get("imgId"));/*
        contentRecommendation.setPath((String[]) resultRow.get("path"));
        contentRecommendation.setProbability((Double) resultRow.get("probability"));*/
        contentRecommendation.setWeight((Double) resultRow.get("weight"));/*
        contentRecommendation.setPathWeight((Double) resultRow.get("pathWeight"));
        contentRecommendation.setInterestWeight((Double) resultRow.get("interestWeight"));*/
        return contentRecommendation;
    }

    public List<ContentRecommendation> generateContentRecommendation(float q, String userId, String searchDepth) {
        Session session = sessionFactory.openSession();
        String contentRecommendationQueryWithSpecifiedDepth = contentRecommendationQuery.replace("{searchDepth}", String.valueOf(searchDepth));
        Result result = session.query(contentRecommendationQueryWithSpecifiedDepth, Map.of(USER_ID_PARAMETER, userId));
        LinkedList<ContentRecommendation> contentRecommendations = mapResult(result);
        double min = contentRecommendations.getFirst().getWeight();
        double max = contentRecommendations.getLast().getWeight();
        double quantile = min + (max - min) * q;
        return contentRecommendations.stream().filter(rec->rec.getWeight() > quantile).collect(Collectors.toList());
    }
}
