package com.mindflow.ai.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mindflow.ai.embedding.EmbeddingService;
import com.mindflow.domain.task.AiTask;
import com.mindflow.domain.task.TaskStatus;
import com.mindflow.domain.task.TaskType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EmbeddingTaskWorkerTest {

    @Test
    void processesPendingEmbeddingTask() {
        EmbeddingService embeddingService = new EmbeddingService((model, text) -> List.of(0.1, 0.2, 0.3));
        EmbeddingTaskWorker worker = new EmbeddingTaskWorker(embeddingService);
        AiTask task = AiTask.embedding(UUID.randomUUID(), "RAG note");

        AiTask processed = worker.process(task);

        assertEquals(TaskType.EMBEDDING, worker.supports());
        assertEquals(TaskStatus.SUCCESS, processed.status());
        assertEquals(3, processed.output().get("dimensions"));
        assertEquals(List.of(0.1, 0.2, 0.3), processed.output().get("vector"));
    }

    @Test
    void marksTaskFailedWhenEmbeddingFails() {
        EmbeddingService embeddingService = new EmbeddingService((model, text) -> {
            throw new IllegalStateException("provider unavailable");
        });
        EmbeddingTaskWorker worker = new EmbeddingTaskWorker(embeddingService);
        AiTask task = AiTask.embedding(UUID.randomUUID(), "RAG note");

        AiTask processed = worker.process(task);

        assertEquals(TaskStatus.FAILED, processed.status());
        assertEquals("provider unavailable", processed.output().get("error"));
    }

    @Test
    void rejectsNonPendingTask() {
        EmbeddingTaskWorker worker = new EmbeddingTaskWorker(new EmbeddingService((model, text) -> List.of(0.1)));
        AiTask task = AiTask.embedding(UUID.randomUUID(), "RAG note");
        task.markRunning();

        assertThrows(IllegalArgumentException.class, () -> worker.process(task));
    }
}
