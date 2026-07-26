package com.mindflow.domain.search;

import java.util.List;

public interface VectorSearchRepository {

    List<KnowledgeSnippet> searchSimilar(List<Double> queryVector, int limit);
}
