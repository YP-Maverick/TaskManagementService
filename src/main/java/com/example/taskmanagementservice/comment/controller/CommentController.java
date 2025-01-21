package com.example.taskmanagementservice.comment.controller;

import com.example.taskmanagementservice.comment.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    // Создать комментарий
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        // TODO Реализация логики для создания комментария
        return ResponseEntity.ok(comment);
    }

    // Получить комментарий по ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        // TODO Реализация поиска комментария по ID
        return ResponseEntity.ok(new Comment());
    }

    // Получить все комментарии
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        // TODO Реализация получения всех комментариев
        return ResponseEntity.ok(List.of());
    }

    // Удалить комментарий
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        // TODO Реализация удаления комментария
        return ResponseEntity.noContent().build();
    }
}

