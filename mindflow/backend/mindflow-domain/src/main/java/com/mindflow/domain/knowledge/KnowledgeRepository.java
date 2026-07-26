package com.mindflow.domain.knowledge;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KnowledgeRepository {

    KnowledgeObject save(KnowledgeObject object);

    Optional<KnowledgeObject> findById(UUID id);

    List<KnowledgeObject> findAll();
}
