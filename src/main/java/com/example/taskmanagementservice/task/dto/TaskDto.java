package com.example.taskmanagementservice.task.dto;

import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

@Value
@Builder
public class TaskDto {

    @Positive
    Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description;

    @NotNull
    TaskStatus status;

    @NotNull
    TaskPriority priority;

    //TODO Автор id не нужен, его извлекаем из CurrentUser
    @Positive
    Long authorId;

    @Positive
    Long performerId;
}
