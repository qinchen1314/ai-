package com.mindflow.application.search;

import java.util.List;

public interface QuestionEmbeddingGateway {

    List<Double> embedQuestion(String question);
}
