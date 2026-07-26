package com.mindflow.application.search;

import com.mindflow.domain.search.KnowledgeSnippet;
import com.mindflow.domain.search.VectorSearchRepository;
import java.util.List;
import java.util.Objects;

public final class VectorSearchApplicationService implements VectorSearchUseCase {

    private final QuestionEmbeddingGateway embeddingGateway;
    private final VectorSearchRepository searchRepository;

    public VectorSearchApplicationService(
            QuestionEmbeddingGateway embeddingGateway,
            VectorSearchRepository searchRepository
    ) {
        this.embeddingGateway = Objects.requireNonNull(embeddingGateway, "embeddingGateway must not be null");
        this.searchRepository = Objects.requireNonNull(searchRepository, "searchRepository must not be null");
    }

    @Override
    public VectorSearchResult search(VectorSearchQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        List<Double> queryVector = embeddingGateway.embedQuestion(query.question());
        validateVector(queryVector);
        List<KnowledgeSnippet> snippets = searchRepository.searchSimilar(queryVector, query.limit());
        return new VectorSearchResult(snippets);
    }

    private static void validateVector(List<Double> vector) {
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("query vector must not be empty");
        }
        if (vector.stream().anyMatch(VectorSearchApplicationService::isInvalidNumber)) {
            throw new IllegalArgumentException("query vector must contain finite numbers");
        }
    }

    private static boolean isInvalidNumber(Double value) {
        return value == null || value.isNaN() || value.isInfinite();
    }
}
