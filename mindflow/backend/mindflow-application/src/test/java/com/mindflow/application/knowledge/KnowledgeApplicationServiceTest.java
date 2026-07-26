package com.mindflow.application.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeRepository;
import com.mindflow.domain.knowledge.KnowledgeStatus;
import com.mindflow.domain.knowledge.KnowledgeType;
import com.mindflow.domain.task.AiTask;
import com.mindflow.domain.task.AiTaskRepository;
import com.mindflow.domain.task.TaskStatus;
import com.mindflow.domain.task.TaskType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class KnowledgeApplicationServiceTest {

    @Test
    void createsKnowledgeObjectThroughRepository() {
        FakeKnowledgeRepository repository = new FakeKnowledgeRepository();
        FakeAiTaskRepository taskRepository = new FakeAiTaskRepository();
        KnowledgeApplicationService service = new KnowledgeApplicationService(repository, taskRepository);

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
        assertEquals(1, taskRepository.tasks.size());
        assertEquals(TaskType.EMBEDDING, taskRepository.tasks.getFirst().type());
        assertEquals(TaskStatus.PENDING, taskRepository.tasks.getFirst().status());
        assertEquals(result.id().toString(), taskRepository.tasks.getFirst().input().get("knowledgeObjectId"));
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

    private static final class FakeAiTaskRepository implements AiTaskRepository {

        private final List<AiTask> tasks = new ArrayList<>();

        @Override
        public AiTask save(AiTask task) {
            tasks.add(task);
            return task;
        }

        @Override
        public Optional<AiTask> findById(UUID id) {
            return tasks.stream().filter(task -> task.id().equals(id)).findFirst();
        }

        @Override
        public List<AiTask> findPendingByType(TaskType type, int limit) {
            return tasks.stream()
                    .filter(task -> task.type() == type)
                    .filter(task -> task.status() == TaskStatus.PENDING)
                    .limit(limit)
                    .toList();
        }
    }
}
