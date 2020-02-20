package org.ius.gradcit.config;

import org.neo4j.ogm.config.Configuration.Builder;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "org.ius.gradcit.database.repository")
public class Neo4jConfig {

    public static final String URL = System.getenv("NEO4J_URL") != null ? System.getenv("NEO4J_URL") : "bolt://neo4j:GoWork17@192.168.0.14:11024";

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        return new Builder().uri(URL).build();
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(getConfiguration(), "org.ius.gradcit.database.domain");
    }
}
