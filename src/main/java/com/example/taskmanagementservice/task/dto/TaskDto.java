package com.example.taskmanagementservice.task.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskDto {

    Long id;
    String title;
    String description;
    String status;
    String taskPriority;
    Long authorId;
    Long performerId;
}
