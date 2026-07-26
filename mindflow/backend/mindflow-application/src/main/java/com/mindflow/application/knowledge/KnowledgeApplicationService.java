package com.mindflow.application.knowledge;

import com.mindflow.domain.knowledge.KnowledgeObject;
import com.mindflow.domain.knowledge.KnowledgeRepository;
import com.mindflow.domain.task.AiTask;
import com.mindflow.domain.task.AiTaskRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeApplicationService implements CreateKnowledgeUseCase {

    private final KnowledgeRepository repository;
    private final Optional<AiTaskRepository> taskRepository;

    public KnowledgeApplicationService(KnowledgeRepository repository) {
        this(repository, Optional.empty());
    }

    public KnowledgeApplicationService(KnowledgeRepository repository, AiTaskRepository taskRepository) {
        this(repository, Optional.of(taskRepository));
    }

    @Autowired
    public KnowledgeApplicationService(KnowledgeRepository repository, Optional<AiTaskRepository> taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
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
        taskRepository.ifPresent(tasks -> tasks.save(AiTask.embedding(saved.id(), saved.content())));
        return new CreateKnowledgeResult(saved.id(), saved.status());
    }
}
