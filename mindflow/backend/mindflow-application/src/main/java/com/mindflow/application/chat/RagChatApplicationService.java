package com.mindflow.application.chat;

import com.mindflow.application.search.HybridSearchQuery;
import com.mindflow.application.search.HybridSearchUseCase;
import com.mindflow.application.search.SearchResult;
import com.mindflow.domain.search.KnowledgeSnippet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class RagChatApplicationService implements RagChatUseCase {

    private static final int DEFAULT_CONTEXT_LIMIT = 5;

    private final Optional<HybridSearchUseCase> searchUseCase;
    private final Optional<ChatCompletionGateway> chatGateway;

    public RagChatApplicationService(HybridSearchUseCase searchUseCase, ChatCompletionGateway chatGateway) {
        this(Optional.of(searchUseCase), Optional.of(chatGateway));
    }

    @Autowired
    public RagChatApplicationService(
            Optional<HybridSearchUseCase> searchUseCase,
            Optional<ChatCompletionGateway> chatGateway
    ) {
        this.searchUseCase = Objects.requireNonNull(searchUseCase, "searchUseCase must not be null");
        this.chatGateway = Objects.requireNonNull(chatGateway, "chatGateway must not be null");
    }

    @Override
    public RagChatResult chat(RagChatCommand command) {
        Objects.requireNonNull(command, "command must not be null");
        HybridSearchUseCase search = searchUseCase.orElseThrow(RagChatApplicationService::missingDependencies);
        ChatCompletionGateway llm = chatGateway.orElseThrow(RagChatApplicationService::missingDependencies);

        SearchResult searchResult = search.search(new HybridSearchQuery(command.question(), DEFAULT_CONTEXT_LIMIT));
        String prompt = buildPrompt(command.question(), searchResult.snippets());
        String answer = llm.complete(prompt);
        List<String> sources = searchResult.snippets().stream()
                .map(KnowledgeSnippet::title)
                .distinct()
                .toList();
        return new RagChatResult(answer, sources);
    }

    private static String buildPrompt(String question, List<KnowledgeSnippet> snippets) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Answer the question using only the provided context.\n\n");
        prompt.append("Question:\n").append(question).append("\n\n");
        prompt.append("Context:\n");
        for (int index = 0; index < snippets.size(); index++) {
            KnowledgeSnippet snippet = snippets.get(index);
            prompt.append("[").append(index + 1).append("] ")
                    .append(snippet.title())
                    .append("\n")
                    .append(snippet.content())
                    .append("\n\n");
        }
        return prompt.toString();
    }

    private static IllegalStateException missingDependencies() {
        return new IllegalStateException("RAG chat dependencies are not configured");
    }
}
