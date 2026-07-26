package com.mindflow.ai.embedding;

import com.mindflow.application.search.QuestionEmbeddingGateway;
import java.util.List;
import java.util.Objects;

public final class EmbeddingQuestionGateway implements QuestionEmbeddingGateway {

    private final EmbeddingService embeddingService;

    public EmbeddingQuestionGateway(EmbeddingService embeddingService) {
        this.embeddingService = Objects.requireNonNull(embeddingService, "embeddingService must not be null");
    }

    @Override
    public List<Double> embedQuestion(String question) {
        return embeddingService.generate(question).values();
    }
}
