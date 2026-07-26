package com.mindflow.application.knowledge;

import com.mindflow.domain.knowledge.KnowledgeStatus;
import java.util.UUID;

public record CreateKnowledgeResult(UUID id, KnowledgeStatus status) {
}
