package com.example.taskmanagementservice.comment.service;

import com.example.taskmanagementservice.comment.model.Comment;
import com.example.taskmanagementservice.comment.repository.CommentRepository;
import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.service.TaskService;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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

        Comment savedComment = commentRepository.save(comment);

        log.info("Successfully createComment with taskId={}, commentId={} by userId={}, email:{}",
                savedComment.getTask().getId(), savedComment.getCommentId(), commentator.getId(), commentator.getEmail()
        );
        return savedComment;
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

        Comment savedComment = commentRepository.save(reply);
        log.info("Successfully createReplyComment with taskId={}, commentId={} by userId={}, email:{}",
                savedComment.getTask().getId(), savedComment.getCommentId(), commentator.getId(), commentator.getEmail()
        );
        return savedComment;
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskIdWithReplies(taskId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        User requester = userService.getCurrentUser();

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment with id " + commentId + " not found.")
        );

        log.info("Successfully getCommentById with taskId={}, commentId={} by userId={}, email:{}",
                comment.getTask().getId(), commentId, requester.getId(), requester.getEmail()
        );
        return comment;
    }

    @Override
    public Comment updateComment(Long commentId, String newContent) {

        User requester = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found."));

        if (!isCommentAuthor(requester, comment)) {
            throw new AuthenticationException(
                    "User must be the author of the comment with id " + commentId + " or an administrator."
            );
        }

        comment.setContent(newContent);
        Comment updatedComment = commentRepository.save(comment);

        log.info("Successfully updateComment with taskId={}, commentId={} by userId={}, email:{}",
                comment.getTask().getId(), commentId, requester.getId(), requester.getEmail()
        );
        return updatedComment;
    }

    @Override
    public void deleteComment(Long commentId) {
        User requester = userService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found."));

        if (!isCommentOwnerOrIsAdmin(requester, comment)) {
            throw new AuthenticationException(
                    "User must be the author of the comment with id " + commentId + " or an administrator."
            );
        }
        commentRepository.deleteById(commentId);
        log.info("Successfully deleteComment with taskId={}, commentId={} by userId={}, email:{}",
                comment.getTask().getId(), commentId, requester.getId(), requester.getEmail()
        );
    }
}
