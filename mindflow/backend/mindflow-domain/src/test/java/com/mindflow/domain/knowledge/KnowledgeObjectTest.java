package com.mindflow.domain.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class KnowledgeObjectTest {

    @Test
    void createsKnowledgeObjectSuccessfully() {
        UUID workspaceId = UUID.randomUUID();

        KnowledgeObject object = KnowledgeObject.create(
                workspaceId,
                KnowledgeType.NOTE,
                " Spring AI Learning ",
                "Spring AI provides LLM abstractions."
        );

        assertNotNull(object.id());
        assertEquals(workspaceId, object.workspaceId());
        assertEquals(KnowledgeType.NOTE, object.type());
        assertEquals("Spring AI Learning", object.title());
        assertEquals("Spring AI provides LLM abstractions.", object.content());
        assertEquals(KnowledgeStatus.CREATED, object.status());
        assertEquals(0.0, object.importance());
        assertNotNull(object.createdAt());
        assertNotNull(object.updatedAt());
    }

    @Test
    void rejectsMissingType() {
        UUID workspaceId = UUID.randomUUID();

        assertThrows(
                NullPointerException.class,
                () -> KnowledgeObject.create(workspaceId, null, "RAG", "Retrieval augmented generation")
        );
    }

    @Test
    void rejectsBlankTitle() {
        UUID workspaceId = UUID.randomUUID();

        assertThrows(
                IllegalArgumentException.class,
                () -> KnowledgeObject.create(workspaceId, KnowledgeType.CONCEPT, " ", "RAG")
        );
    }

    @Test
    void updatesSummaryAndStatus() {
        KnowledgeObject object = KnowledgeObject.create(
                UUID.randomUUID(),
                KnowledgeType.CONCEPT,
                "RAG",
                "Retrieval augmented generation"
        );

        object.updateSummary(" Uses retrieved context to answer questions. ");
        object.markActive();

        assertEquals("Uses retrieved context to answer questions.", object.summary());
        assertEquals(KnowledgeStatus.ACTIVE, object.status());
        assertTrue(!object.updatedAt().isBefore(object.createdAt()));
    }

    @Test
    void rejectsImportanceOutsideRange() {
        KnowledgeObject object = KnowledgeObject.create(
                UUID.randomUUID(),
                KnowledgeType.IDEA,
                "Personal knowledge engine",
                "An AI-native way to manage knowledge."
        );

        assertThrows(IllegalArgumentException.class, () -> object.updateImportance(1.1));
        assertThrows(IllegalArgumentException.class, () -> object.updateImportance(-0.1));
    }
}
