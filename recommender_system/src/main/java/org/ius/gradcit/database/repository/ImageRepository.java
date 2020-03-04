package org.ius.gradcit.database.repository;

import org.ius.gradcit.database.domain.node.Image;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends Neo4jRepository<Image, Long> {
    Image findByExternalId(String externalId);
}
