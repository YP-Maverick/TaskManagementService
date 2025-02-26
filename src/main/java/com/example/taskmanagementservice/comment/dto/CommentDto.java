package com.example.taskmanagementservice.comment.dto;

import com.example.taskmanagementservice.user.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;
    private UserDto author;
    private String content;
    private LocalDateTime timestamp;
    private List<CommentDto> replies;
}

