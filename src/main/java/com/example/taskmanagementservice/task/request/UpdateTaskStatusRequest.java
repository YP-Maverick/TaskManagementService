package com.example.taskmanagementservice.task.request;

import com.example.taskmanagementservice.task.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateTaskStatusRequest {

    @NotNull
    Integer taskId;

    @NotNull(message = "Status is mandatory")
    TaskStatus status;
}
