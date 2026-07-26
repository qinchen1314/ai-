package com.mindflow.infrastructure.persistence.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeRepository;
import com.mindflow.domain.knowledge.KnowledgeStatus;
import com.mindflow.domain.knowledge.KnowledgeType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JpaKnowledgeRepositoryTest {

    @Test
    void savesAndFindsKnowledgeObjectById() {
        FakeKnowledgeObjectJpaRepository fakeRepository = new FakeKnowledgeObjectJpaRepository();
        KnowledgeRepository repository = new JpaKnowledgeRepository(fakeRepository);
        KnowledgeObject object = KnowledgeObject.create(
                UUID.randomUUID(),
                KnowledgeType.NOTE,
                "RAG Learning",
                "Retrieval augmented generation"
        );

        KnowledgeObject saved = repository.save(object);
        Optional<KnowledgeObject> found = repository.findById(saved.id());

        assertTrue(found.isPresent());
        assertEquals(saved.id(), found.orElseThrow().id());
        assertEquals("RAG Learning", found.orElseThrow().title());
        assertEquals(KnowledgeStatus.CREATED, found.orElseThrow().status());
    }

    @Test
    void returnsAllKnowledgeObjects() {
        FakeKnowledgeObjectJpaRepository fakeRepository = new FakeKnowledgeObjectJpaRepository();
        KnowledgeRepository repository = new JpaKnowledgeRepository(fakeRepository);
        repository.save(KnowledgeObject.create(UUID.randomUUID(), KnowledgeType.NOTE, "Note", "Content"));
        repository.save(KnowledgeObject.create(UUID.randomUUID(), KnowledgeType.CONCEPT, "RAG", "Concept"));

        List<KnowledgeObject> objects = repository.findAll();

        assertEquals(2, objects.size());
        assertEquals(List.of("Note", "RAG"), objects.stream().map(KnowledgeObject::title).toList());
    }

    private static final class FakeKnowledgeObjectJpaRepository implements KnowledgeObjectJpaRepository {

        private final Map<UUID, KnowledgeObjectJpaEntity> rows = new LinkedHashMap<>();

        @Override
        public KnowledgeObjectJpaEntity save(KnowledgeObjectJpaEntity entity) {
            rows.put(entity.id(), entity);
            return entity;
        }

        @Override
        public Optional<KnowledgeObjectJpaEntity> findById(UUID id) {
            return Optional.ofNullable(rows.get(id));
        }

        @Override
        public List<KnowledgeObjectJpaEntity> findAll() {
            return new ArrayList<>(rows.values());
        }
    }
}
