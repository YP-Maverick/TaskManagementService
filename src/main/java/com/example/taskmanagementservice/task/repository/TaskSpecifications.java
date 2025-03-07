package com.example.taskmanagementservice.task.repository;

import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> withStatus(TaskStatus status) {
        return (root, query, cb) ->
                status != null ? cb.equal(root.get("status"), status) : null;
    }

    public static Specification<Task> withPriority(TaskPriority priority) {
        return (root, query, cb) ->
                priority != null ? cb.equal(root.get("priority"), priority) : null;
    }

}