package com.mindflow.application.knowledge;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeRepository;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeApplicationService implements CreateKnowledgeUseCase {

    private final KnowledgeRepository repository;

    public KnowledgeApplicationService(KnowledgeRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreateKnowledgeResult create(CreateKnowledgeCommand command) {
        KnowledgeObject object = KnowledgeObject.create(
                command.workspaceId(),
                command.type(),
                command.title(),
                command.content()
        );
        KnowledgeObject saved = repository.save(object);
        return new CreateKnowledgeResult(saved.id(), saved.status());
    }
}
