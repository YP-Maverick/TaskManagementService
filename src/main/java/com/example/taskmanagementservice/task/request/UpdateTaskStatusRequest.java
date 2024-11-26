package com.example.taskmanagementservice.task.request;

import com.example.taskmanagementservice.task.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTaskStatusRequest {

    @NotNull(message = "Status is mandatory")
    TaskStatus status;
}
