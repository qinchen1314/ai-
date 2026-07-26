package com.mindflow.ai.task;

import com.mindflow.domain.task.AiTask;
import com.mindflow.domain.task.TaskType;

public interface TaskWorker {

    TaskType supports();

    AiTask process(AiTask task);
}
