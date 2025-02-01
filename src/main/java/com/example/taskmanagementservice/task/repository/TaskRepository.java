package com.example.taskmanagementservice.task.repository;

import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findByPerformer(User performer);

    List<Task> findByAuthor(User currentUser);

    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}