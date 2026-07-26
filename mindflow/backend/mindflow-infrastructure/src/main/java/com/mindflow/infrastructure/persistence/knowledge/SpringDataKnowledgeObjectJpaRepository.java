package com.mindflow.infrastructure.persistence.knowledge;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataKnowledgeObjectJpaRepository
        extends JpaRepository<KnowledgeObjectJpaEntity, UUID>, KnowledgeObjectJpaRepository {
}
