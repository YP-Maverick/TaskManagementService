package com.example.taskmanagementservice.comment.controller;

import com.example.taskmanagementservice.comment.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        // TODO Реализация логики для создания комментария
        return ResponseEntity.ok(comment);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        // TODO Коммент может изменить только автор
        return ResponseEntity.ok(comment);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        // TODO Реализация поиска комментария по ID
        return ResponseEntity.ok(new Comment());
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(
            @PathVariable Long taskId
    ) {
        // TODO Реализация получения всех комментариев
        return ResponseEntity.ok(List.of());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        // TODO Коммент может удалить автор или любой админ
        return ResponseEntity.noContent().build();
    }
}

