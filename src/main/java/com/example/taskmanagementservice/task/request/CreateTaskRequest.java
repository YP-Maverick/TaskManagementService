package com.example.taskmanagementservice.task.request;

import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTaskRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description;

    @NotBlank
    TaskStatus status;

    @NotBlank
    TaskPriority priority;

    @NotNull
    Long authorId;

    @NotNull
    Long performerId;
}
