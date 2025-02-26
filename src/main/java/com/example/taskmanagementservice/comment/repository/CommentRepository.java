package com.example.taskmanagementservice.comment.repository;

import com.example.taskmanagementservice.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.task.id = :taskId AND c.parentComment IS NULL")
    List<Comment> findByTaskIdWithReplies(@Param("taskId") Long taskId);
}