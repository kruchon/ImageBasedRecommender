package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.node.Thematics;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThematicsRepository extends Neo4jRepository<Thematics, Long> {
    Optional<Thematics> findByWord(String word);
}