package com.mindflow.infrastructure.persistence.knowledge;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnBean(KnowledgeObjectJpaRepository.class)
public class JpaKnowledgeRepository implements KnowledgeRepository {

    private final KnowledgeObjectJpaRepository repository;

    public JpaKnowledgeRepository(KnowledgeObjectJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public KnowledgeObject save(KnowledgeObject object) {
        KnowledgeObjectJpaEntity saved = repository.save(KnowledgeObjectJpaEntity.fromDomain(object));
        return saved.toDomain();
    }

    @Override
    public Optional<KnowledgeObject> findById(UUID id) {
        return repository.findById(id).map(KnowledgeObjectJpaEntity::toDomain);
    }

    @Override
    public List<KnowledgeObject> findAll() {
        return repository.findAll()
                .stream()
                .map(KnowledgeObjectJpaEntity::toDomain)
                .toList();
    }
}
