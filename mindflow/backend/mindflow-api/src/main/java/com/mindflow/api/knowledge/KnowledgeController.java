package com.mindflow.api.knowledge;

import com.mindflow.application.knowledge.CreateKnowledgeCommand;
import com.mindflow.application.knowledge.CreateKnowledgeResult;
import com.mindflow.application.knowledge.CreateKnowledgeUseCase;
import com.mindflow.domain.knowledge.KnowledgeType;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final CreateKnowledgeUseCase service;

    public KnowledgeController(CreateKnowledgeUseCase service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreateKnowledgeResponse> create(@RequestBody CreateKnowledgeRequest request) {
        CreateKnowledgeResult result = service.create(toCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateKnowledgeResponse(result.id(), result.status().name()));
    }

    private static CreateKnowledgeCommand toCommand(CreateKnowledgeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "request body is required");
        }

        return new CreateKnowledgeCommand(
                parseWorkspaceId(request.workspaceId()),
                parseKnowledgeType(request.type()),
                requireText(request.title(), "title"),
                requireText(request.content(), "content")
        );
    }

    private static UUID parseWorkspaceId(String workspaceId) {
        if (workspaceId == null || workspaceId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "workspaceId is required");
        }

        try {
            return UUID.fromString(workspaceId);
        } catch (IllegalArgumentException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "workspaceId must be a valid UUID", error);
        }
    }

    private static KnowledgeType parseKnowledgeType(String type) {
        if (type == null || type.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type is required");
        }

        try {
            return KnowledgeType.valueOf(type);
        } catch (IllegalArgumentException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type is not supported", error);
        }
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }

        return value;
    }
}
