package com.example.taskmanagementservice.comment.service;

import com.example.taskmanagementservice.comment.model.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Long taskId, String content);
    Comment createReplyComment(Long taskId, Long parentCommentId, String content);

    List<Comment> getCommentsByTaskId(Long taskId);
    Comment getCommentById(Long commentId);

    Comment updateComment(Long commentId, String newContent);

    void deleteComment(Long commentId);
}
