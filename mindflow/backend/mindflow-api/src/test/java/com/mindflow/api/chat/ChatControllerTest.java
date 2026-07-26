package com.mindflow.api.chat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mindflow.application.chat.RagChatResult;
import com.mindflow.application.chat.RagChatUseCase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = ChatController.class, properties = "mindflow.persistence.enabled=false")
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RagChatUseCase chatUseCase;

    @Test
    void chatsWithRagSources() throws Exception {
        when(chatUseCase.chat(any())).thenReturn(new RagChatResult(
                "Spring AI can build RAG apps.",
                List.of("SpringAI.md")
        ));

        mockMvc.perform(post("/api/v1/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "question": "How do I build RAG?"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Spring AI can build RAG apps."))
                .andExpect(jsonPath("$.sources[0]").value("SpringAI.md"));
    }

    @Test
    void rejectsBlankQuestion() throws Exception {
        mockMvc.perform(post("/api/v1/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "question": " "
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
