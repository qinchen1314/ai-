package com.mindflow.domain.search;

import java.util.List;

public interface FullTextSearchRepository {

    List<KnowledgeSnippet> searchByText(String query, int limit);
}
