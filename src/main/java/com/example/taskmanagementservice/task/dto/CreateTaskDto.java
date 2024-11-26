package com.example.taskmanagementservice.task.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class CreateTaskDto {

    String title;
    String description;
    String status;
    String priority;
    Long authorId;
    Long performerId;
}
