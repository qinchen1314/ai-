package com.mindflow.infrastructure.persistence.knowledge;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KnowledgeObjectJpaRepository {

    KnowledgeObjectJpaEntity save(KnowledgeObjectJpaEntity entity);

    Optional<KnowledgeObjectJpaEntity> findById(UUID id);

    List<KnowledgeObjectJpaEntity> findAll();
}
