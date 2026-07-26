package com.mindflow.application.knowledge;

import com.mindflow.domain.knowledge.KnowledgeType;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MarkdownKnowledgeParser {

    private static final String DEFAULT_TITLE = "Untitled Markdown Note";

    public MarkdownDocument parse(String markdown) {
        String content = requireMarkdown(markdown);
        return new MarkdownDocument(extractTitle(content), content);
    }

    public CreateKnowledgeCommand toCreateCommand(UUID workspaceId, String markdown) {
        MarkdownDocument document = parse(markdown);
        return new CreateKnowledgeCommand(
                workspaceId,
                KnowledgeType.NOTE,
                document.title(),
                document.content()
        );
    }

    private static String requireMarkdown(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            throw new IllegalArgumentException("markdown must not be blank");
        }

        return markdown.trim();
    }

    private static String extractTitle(String markdown) {
        return Arrays.stream(markdown.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .filter(line -> line.startsWith("# "))
                .map(line -> line.substring(2).trim())
                .filter(title -> !title.isBlank())
                .findFirst()
                .orElse(DEFAULT_TITLE);
    }
}
