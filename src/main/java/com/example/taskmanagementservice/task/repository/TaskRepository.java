package com.example.taskmanagementservice.task.repository;

import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByPerformer(User performer);

    List<Task> findByAuthor(User currentUser);
}