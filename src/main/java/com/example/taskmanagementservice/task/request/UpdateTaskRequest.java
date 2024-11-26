package com.example.taskmanagementservice.task.request;

import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Builder
public class UpdateTaskRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description;

    @NotNull(message = "Status is mandatory")
    TaskStatus status;

    @NotNull(message = "Priority is mandatory")
    TaskPriority priority;

    @NotNull(message = "Performer ID is mandatory")
    Long performerId;
}