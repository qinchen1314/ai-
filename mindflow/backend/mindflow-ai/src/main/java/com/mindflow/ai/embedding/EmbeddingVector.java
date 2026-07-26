package com.mindflow.ai.embedding;

import java.util.List;

public record EmbeddingVector(List<Double> values) {

    public EmbeddingVector {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("embedding values must not be empty");
        }

        values = List.copyOf(values);
        if (values.stream().anyMatch(EmbeddingVector::isInvalidNumber)) {
            throw new IllegalArgumentException("embedding values must be finite numbers");
        }
    }

    public int dimensions() {
        return values.size();
    }

    private static boolean isInvalidNumber(Double value) {
        return value == null || value.isNaN() || value.isInfinite();
    }
}
