package com.example.taskmanagementservice.comment.repository;

import com.example.taskmanagementservice.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
