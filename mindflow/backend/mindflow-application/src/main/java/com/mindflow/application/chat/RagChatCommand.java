package com.mindflow.application.chat;

public record RagChatCommand(String question) {

    public RagChatCommand {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("question must not be blank");
        }

        question = question.trim();
    }
}
