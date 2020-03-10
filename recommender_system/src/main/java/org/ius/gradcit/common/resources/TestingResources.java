package org.ius.gradcit.common.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

@Configuration
public class TestingResources {

    private static final ObjectMapper om = new ObjectMapper();

    @Bean
    public Map<String, Object> testUsers() throws IOException {
        String testUsers = ResourceReader.readFileToString("testing/testUsers.json");
        return om.readValue(testUsers, Map.class);
    }

}
