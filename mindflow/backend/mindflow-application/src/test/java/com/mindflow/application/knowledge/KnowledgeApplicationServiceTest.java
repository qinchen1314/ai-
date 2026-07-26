package com.mindflow.application.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeRepository;
import com.mindflow.domain.knowledge.KnowledgeStatus;
import com.mindflow.domain.knowledge.KnowledgeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KnowledgeApplicationServiceTest {

    @Test
    void createsKnowledgeObjectThroughRepository() {
        FakeKnowledgeRepository repository = new FakeKnowledgeRepository();
        KnowledgeApplicationService service = new KnowledgeApplicationService(repository);

        CreateKnowledgeResult result = service.create(new CreateKnowledgeCommand(
                UUID.randomUUID(),
                KnowledgeType.NOTE,
                "RAG Learning",
                "Retrieval augmented generation"
        ));

        assertNotNull(result.id());
        assertEquals(KnowledgeStatus.CREATED, result.status());
        assertEquals(1, repository.objects.size());
        assertEquals("RAG Learning", repository.objects.getFirst().title());
    }

    private static final class FakeKnowledgeRepository implements KnowledgeRepository {

        private final List<KnowledgeObject> objects = new ArrayList<>();

        @Override
        public KnowledgeObject save(KnowledgeObject object) {
            objects.add(object);
            return object;
        }

        @Override
        public Optional<KnowledgeObject> findById(UUID id) {
            return objects.stream().filter(object -> object.id().equals(id)).findFirst();
        }

        @Override
        public List<KnowledgeObject> findAll() {
            return List.copyOf(objects);
        }
    }
}
