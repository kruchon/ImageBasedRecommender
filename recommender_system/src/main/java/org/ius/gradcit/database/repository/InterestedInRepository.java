package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.relationship.InterestedIn;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestedInRepository extends Neo4jRepository<InterestedIn, Long> {
    @Query("MATCH (u:User)-[r:INTERESTED_IN]->(t:Token) WHERE id(u)={userId} AND id(t)={thematicsId} RETURN r")
    Optional<InterestedIn> findByUserIdAndThematicsId(Long userId, Long thematicsId);
}
