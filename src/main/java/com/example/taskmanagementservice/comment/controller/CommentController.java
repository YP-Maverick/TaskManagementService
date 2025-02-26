package com.example.taskmanagementservice.comment.controller;

import com.example.taskmanagementservice.comment.dto.CommentDto;
import com.example.taskmanagementservice.comment.dto.CreateCommentRequest;
import com.example.taskmanagementservice.comment.dto.UpdateCommentRequest;
import com.example.taskmanagementservice.comment.model.CommentMapper;
import com.example.taskmanagementservice.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                        commentService.createComment(taskId, request.getContent())
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/{taskId}/comments/{parentCommentId}/replies")
    public ResponseEntity<CommentDto> createReplyComment(
            @PathVariable Long taskId,
            @PathVariable Long parentCommentId,
            @Valid @RequestBody CreateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                    commentService.createReplyComment(
                                taskId,
                                parentCommentId,
                                request.getContent())
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByTaskId(
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(
                commentMapper.toDtoList(
                    commentService.getCommentsByTaskId(taskId)
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                        commentService.getCommentById(commentId)
                )
        );
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                        commentService.updateComment(commentId, request.getNewContent())
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

