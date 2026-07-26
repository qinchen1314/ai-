package com.mindflow.application.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mindflow.application.search.HybridSearchQuery;
import com.mindflow.application.search.HybridSearchUseCase;
import com.mindflow.application.search.SearchResult;
import com.mindflow.domain.search.KnowledgeSnippet;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RagChatApplicationServiceTest {

    @Test
    void answersQuestionWithRetrievedContextAndSources() {
        FakeHybridSearch search = new FakeHybridSearch(new SearchResult(List.of(
                new KnowledgeSnippet(UUID.randomUUID(), "SpringAI.md", "Spring AI supports LLM apps.", 0.91),
                new KnowledgeSnippet(UUID.randomUUID(), "RAG.md", "RAG grounds answers in context.", 0.87)
        )));
        FakeChatCompletion chatCompletion = new FakeChatCompletion("Use Spring AI with retrieved context.");
        RagChatApplicationService service = new RagChatApplicationService(search, chatCompletion);

        RagChatResult result = service.chat(new RagChatCommand("How do I build RAG?"));

        assertEquals("How do I build RAG?", search.lastQuery.question());
        assertEquals(5, search.lastQuery.limit());
        assertEquals("Use Spring AI with retrieved context.", result.answer());
        assertEquals(List.of("SpringAI.md", "RAG.md"), result.sources());
        assertEquals(true, chatCompletion.lastPrompt.contains("Spring AI supports LLM apps."));
        assertEquals(true, chatCompletion.lastPrompt.contains("How do I build RAG?"));
    }

    @Test
    void rejectsBlankQuestion() {
        assertThrows(IllegalArgumentException.class, () -> new RagChatCommand(" "));
    }

    @Test
    void failsClearlyWhenDependenciesAreMissing() {
        RagChatApplicationService service = new RagChatApplicationService(java.util.Optional.empty(), java.util.Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.chat(new RagChatCommand("RAG?")));
    }

    private static final class FakeHybridSearch implements HybridSearchUseCase {

        private final SearchResult result;
        private HybridSearchQuery lastQuery;

        private FakeHybridSearch(SearchResult result) {
            this.result = result;
        }

        @Override
        public SearchResult search(HybridSearchQuery query) {
            lastQuery = query;
            return result;
        }
    }

    private static final class FakeChatCompletion implements ChatCompletionGateway {

        private final String answer;
        private String lastPrompt;

        private FakeChatCompletion(String answer) {
            this.answer = answer;
        }

        @Override
        public String complete(String prompt) {
            lastPrompt = prompt;
            return answer;
        }
    }
}
