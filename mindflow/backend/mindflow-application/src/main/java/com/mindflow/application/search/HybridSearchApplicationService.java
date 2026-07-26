package com.mindflow.application.search;

import com.mindflow.domain.search.FullTextSearchRepository;
import com.mindflow.domain.search.KnowledgeSnippet;
import com.mindflow.domain.search.VectorSearchRepository;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class HybridSearchApplicationService implements HybridSearchUseCase {

    private final QuestionEmbeddingGateway embeddingGateway;
    private final VectorSearchRepository vectorSearchRepository;
    private final FullTextSearchRepository fullTextSearchRepository;

    public HybridSearchApplicationService(
            QuestionEmbeddingGateway embeddingGateway,
            VectorSearchRepository vectorSearchRepository,
            FullTextSearchRepository fullTextSearchRepository
    ) {
        this.embeddingGateway = Objects.requireNonNull(embeddingGateway, "embeddingGateway must not be null");
        this.vectorSearchRepository = Objects.requireNonNull(vectorSearchRepository, "vectorSearchRepository must not be null");
        this.fullTextSearchRepository = Objects.requireNonNull(fullTextSearchRepository, "fullTextSearchRepository must not be null");
    }

    @Override
    public SearchResult search(HybridSearchQuery query) {
        Objects.requireNonNull(query, "query must not be null");

        List<Double> queryVector = embeddingGateway.embedQuestion(query.question());
        validateVector(queryVector);

        Map<UUID, KnowledgeSnippet> merged = new LinkedHashMap<>();
        merge(merged, vectorSearchRepository.searchSimilar(queryVector, query.limit()));
        merge(merged, fullTextSearchRepository.searchByText(query.question(), query.limit()));

        List<KnowledgeSnippet> snippets = merged.values().stream()
                .sorted(Comparator.comparingDouble(KnowledgeSnippet::score).reversed())
                .limit(query.limit())
                .toList();
        return new SearchResult(snippets);
    }

    private static void merge(Map<UUID, KnowledgeSnippet> merged, List<KnowledgeSnippet> snippets) {
        if (snippets == null) {
            return;
        }

        for (KnowledgeSnippet snippet : snippets) {
            merged.merge(snippet.knowledgeObjectId(), snippet, HybridSearchApplicationService::higherScore);
        }
    }

    private static KnowledgeSnippet higherScore(KnowledgeSnippet first, KnowledgeSnippet second) {
        return first.score() >= second.score() ? first : second;
    }

    private static void validateVector(List<Double> vector) {
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("query vector must not be empty");
        }
        if (vector.stream().anyMatch(HybridSearchApplicationService::isInvalidNumber)) {
            throw new IllegalArgumentException("query vector must contain finite numbers");
        }
    }

    private static boolean isInvalidNumber(Double value) {
        return value == null || value.isNaN() || value.isInfinite();
    }
}
