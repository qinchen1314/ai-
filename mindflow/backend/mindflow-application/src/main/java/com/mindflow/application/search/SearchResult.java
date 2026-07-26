package com.mindflow.application.search;

import com.mindflow.domain.search.KnowledgeSnippet;
import java.util.List;

public record SearchResult(List<KnowledgeSnippet> snippets) {

    public SearchResult {
        if (snippets == null) {
            throw new IllegalArgumentException("snippets must not be null");
        }

        snippets = List.copyOf(snippets);
    }
}
