package com.mindflow.application.chat;

import java.util.List;

public record RagChatResult(String answer, List<String> sources) {

    public RagChatResult {
        if (answer == null || answer.isBlank()) {
            throw new IllegalArgumentException("answer must not be blank");
        }
        if (sources == null) {
            throw new IllegalArgumentException("sources must not be null");
        }

        answer = answer.trim();
        sources = List.copyOf(sources);
    }
}
