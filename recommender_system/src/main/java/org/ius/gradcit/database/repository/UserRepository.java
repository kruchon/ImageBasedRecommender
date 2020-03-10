package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findByExternalId(String externalId);
    Long deleteByExternalId(String externalId);
}
