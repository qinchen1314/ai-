package com.mindflow.infrastructure.persistence;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(prefix = "mindflow.persistence", name = "enabled", havingValue = "true", matchIfMissing = true)
@EntityScan("com.mindflow.infrastructure.persistence")
@EnableJpaRepositories("com.mindflow.infrastructure.persistence")
public class PersistenceConfiguration {
}
