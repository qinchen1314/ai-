package com.mindflow.api;

import com.mindflow.domain.knowledge.KnowledgeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration",
        "mindflow.persistence.enabled=false"
})
class MindFlowApplicationTests {

    @MockBean
    private KnowledgeRepository knowledgeRepository;

    @Test
    void contextLoads() {
    }
}
