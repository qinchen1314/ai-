package com.mindflow.api.chat;

import com.mindflow.application.chat.RagChatCommand;
import com.mindflow.application.chat.RagChatResult;
import com.mindflow.application.chat.RagChatUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/chat")
public final class ChatController {

    private final RagChatUseCase chatUseCase;

    public ChatController(RagChatUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        RagChatResult result = chatUseCase.chat(new RagChatCommand(requireQuestion(request)));
        return new ChatResponse(result.answer(), result.sources());
    }

    private static String requireQuestion(ChatRequest request) {
        if (request == null || request.question() == null || request.question().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "question is required");
        }

        return request.question();
    }
}
