package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.relationship.RecognizedIn;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecognizedInRepository extends Neo4jRepository<RecognizedIn, Long> {
    @Query("MATCH(i:Image)<-[r:RECOGNIZED_IN]-(:Token) WHERE i.externalId={imageId} RETURN id(r)")
    List<Long> getByImageExternalId(String imageId);
}
