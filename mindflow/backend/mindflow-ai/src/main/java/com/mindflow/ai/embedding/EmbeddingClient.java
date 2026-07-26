package com.mindflow.ai.embedding;

import java.util.List;

public interface EmbeddingClient {

    List<Double> embed(String model, String text);
}
