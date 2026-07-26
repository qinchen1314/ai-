package com.mindflow.application.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mindflow.domain.knowledge.KnowledgeType;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MarkdownKnowledgeParserTest {

    private final MarkdownKnowledgeParser parser = new MarkdownKnowledgeParser();

    @Test
    void parsesTitleFromFirstHeading() {
        MarkdownDocument document = parser.parse("""
                # RAG Learning

                Retrieval augmented generation notes.
                """);

        assertEquals("RAG Learning", document.title());
        assertEquals("# RAG Learning\n\nRetrieval augmented generation notes.", document.content());
    }

    @Test
    void usesDefaultTitleWhenHeadingIsMissing() {
        MarkdownDocument document = parser.parse("Retrieval augmented generation notes.");

        assertEquals("Untitled Markdown Note", document.title());
    }

    @Test
    void convertsMarkdownToCreateKnowledgeCommand() {
        UUID workspaceId = UUID.randomUUID();

        CreateKnowledgeCommand command = parser.toCreateCommand(workspaceId, "# RAG\n\nContent");

        assertEquals(workspaceId, command.workspaceId());
        assertEquals(KnowledgeType.NOTE, command.type());
        assertEquals("RAG", command.title());
        assertEquals("# RAG\n\nContent", command.content());
    }

    @Test
    void rejectsBlankMarkdown() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(" "));
    }
}
