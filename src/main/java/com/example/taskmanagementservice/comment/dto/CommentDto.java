package com.example.taskmanagementservice.comment.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentDto {

    Long commentId;

    Long taskId;

    String content;

    LocalDateTime timestamp;
}
