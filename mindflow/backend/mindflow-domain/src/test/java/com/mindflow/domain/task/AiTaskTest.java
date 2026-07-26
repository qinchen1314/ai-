package com.mindflow.domain.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AiTaskTest {

    @Test
    void createsPendingEmbeddingTask() {
        UUID objectId = UUID.randomUUID();

        AiTask task = AiTask.embedding(objectId, " Knowledge content ");

        assertNotNull(task.id());
        assertEquals(TaskType.EMBEDDING, task.type());
        assertEquals(TaskStatus.PENDING, task.status());
        assertEquals(objectId.toString(), task.input().get("knowledgeObjectId"));
        assertEquals("Knowledge content", task.input().get("content"));
        assertNotNull(task.createdAt());
        assertNotNull(task.updatedAt());
    }

    @Test
    void movesThroughRunningAndSuccessStates() {
        AiTask task = AiTask.embedding(UUID.randomUUID(), "RAG note");

        task.markRunning();
        task.markSuccess(Map.of("dimensions", 3));

        assertEquals(TaskStatus.SUCCESS, task.status());
        assertEquals(3, task.output().get("dimensions"));
    }

    @Test
    void recordsFailureReason() {
        AiTask task = AiTask.embedding(UUID.randomUUID(), "RAG note");

        task.markRunning();
        task.markFailed("embedding provider unavailable");

        assertEquals(TaskStatus.FAILED, task.status());
        assertEquals("embedding provider unavailable", task.output().get("error"));
    }

    @Test
    void rejectsStartingFinishedTask() {
        AiTask task = AiTask.embedding(UUID.randomUUID(), "RAG note");
        task.markRunning();
        task.markSuccess(Map.of("dimensions", 1));

        assertThrows(IllegalStateException.class, task::markRunning);
    }
}
