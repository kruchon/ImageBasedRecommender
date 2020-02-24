package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.relationship.InterestedIn;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestedInRepository extends Neo4jRepository<InterestedIn, Long> {
}
