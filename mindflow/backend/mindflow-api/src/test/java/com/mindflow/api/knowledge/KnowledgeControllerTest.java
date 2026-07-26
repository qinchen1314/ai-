package com.mindflow.api.knowledge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mindflow.application.knowledge.CreateKnowledgeResult;
import com.mindflow.application.knowledge.CreateKnowledgeUseCase;
import com.mindflow.domain.knowledge.KnowledgeStatus;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = KnowledgeController.class, properties = "mindflow.persistence.enabled=false")
class KnowledgeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateKnowledgeUseCase service;

    @Test
    void createsKnowledgeObject() throws Exception {
        UUID id = UUID.randomUUID();
        UUID workspaceId = UUID.randomUUID();
        when(service.create(any())).thenReturn(new CreateKnowledgeResult(id, KnowledgeStatus.CREATED));

        mockMvc.perform(post("/api/v1/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "title": "RAG Learning",
                                  "type": "NOTE",
                                  "content": "Retrieval augmented generation"
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void rejectsUnsupportedKnowledgeType() throws Exception {
        mockMvc.perform(post("/api/v1/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "title": "RAG Learning",
                                  "type": "UNKNOWN",
                                  "content": "Retrieval augmented generation"
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/v1/knowledge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "title": " ",
                                  "type": "NOTE",
                                  "content": "Retrieval augmented generation"
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }
}
