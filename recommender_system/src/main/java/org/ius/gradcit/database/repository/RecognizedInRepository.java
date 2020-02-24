package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.relationship.RecognizedIn;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecognizedInRepository extends Neo4jRepository<RecognizedIn, Long> {
    @Query("MATCH(:Token{ word: {interestWord} })-[r:RECOGNIZED_IN]->(:Image{externalId: {imageExternalId} }) RETURN r")
    RecognizedIn findByImageExternalIdAndInterestWord(@Param("imageExternalId") String imageExternalId,
                                                                @Param("interestWord") String interestWord);
}
