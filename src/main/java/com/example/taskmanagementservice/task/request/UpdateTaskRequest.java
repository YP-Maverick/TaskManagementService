package com.example.taskmanagementservice.task.request;

import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
@Builder
public class UpdateTaskRequest {

    @Positive
    Long taskId;

    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description;

    TaskStatus status;

    TaskPriority priority;

    @Positive
    Integer performerId;
}