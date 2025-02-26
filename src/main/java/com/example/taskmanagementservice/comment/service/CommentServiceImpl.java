package com.example.taskmanagementservice.comment.service;

import com.example.taskmanagementservice.comment.model.Comment;
import com.example.taskmanagementservice.comment.repository.CommentRepository;
import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.repository.TaskRepository;
import com.example.taskmanagementservice.task.service.TaskService;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final TaskService taskService;

    private final CommentRepository commentRepository;

    private boolean isCommentAuthor(User user, Comment comment) {
        return comment.getAuthor().equals(user);
    }

    private boolean isCommentOwnerOrIsAdmin(User user, Comment comment) {
        return isCommentAuthor(user, comment) || userService.isAdmin(user);
    }

    public Comment createComment(Long taskId, String content) {

        User commentator = userService.getCurrentUser();
        Task task = taskService.getTaskById(taskId);

        Comment comment = Comment.builder()
                .task(task)
                .author(commentator)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    public Comment createReplyComment(Long taskId, Long parentCommentId, String content) {

        User commentator = userService.getCurrentUser();

        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found with id: " + parentCommentId));

        if (!parentComment.getTask().getId().equals(taskId)) {
            throw new IllegalArgumentException("Parent comment does not belong to the task");
        }

        Comment reply = Comment.builder()
                .task(parentComment.getTask())
                .author(commentator)
                .content(content)
                .createdAt(LocalDateTime.now())
                .parentComment(parentComment)
                .build();

        return commentRepository.save(reply);
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskIdWithReplies(taskId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id " + commentId + " not found.")
        );
    }

    @Override
    public Comment updateComment(Long commentId, String newContent) {

        User currentUser = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found."));

        if (!isCommentAuthor(currentUser, comment)) {
            throw new AuthenticationException(
                    "User must be the author of the comment with id " + commentId + " or an administrator."
            );
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        User currentUser = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found."));

        if (!isCommentOwnerOrIsAdmin(currentUser, comment)) {
            throw new AuthenticationException(
                    "User must be the author of the comment with id " + commentId + " or an administrator."
            );
        }
        commentRepository.deleteById(commentId);
    }
}
