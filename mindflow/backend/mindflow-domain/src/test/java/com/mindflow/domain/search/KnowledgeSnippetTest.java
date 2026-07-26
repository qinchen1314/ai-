package com.mindflow.domain.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class KnowledgeSnippetTest {

    @Test
    void createsSnippet() {
        UUID id = UUID.randomUUID();

        KnowledgeSnippet snippet = new KnowledgeSnippet(id, " RAG ", " Retrieval augmented generation ", 0.82);

        assertEquals(id, snippet.knowledgeObjectId());
        assertEquals("RAG", snippet.title());
        assertEquals("Retrieval augmented generation", snippet.content());
        assertEquals(0.82, snippet.score());
    }

    @Test
    void rejectsInvalidScore() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new KnowledgeSnippet(UUID.randomUUID(), "RAG", "content", 1.1)
        );
    }
}
