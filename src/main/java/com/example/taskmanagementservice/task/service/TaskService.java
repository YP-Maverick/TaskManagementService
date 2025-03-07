package com.example.taskmanagementservice.task.service;

import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.request.UpdateTaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Task createTask(CreateTaskRequest request);

    Task getTaskById(Long taskId);
    Page<Task> getAllTasks(TaskStatus status, TaskPriority priority, Pageable pageable);
    List<Task> getTasksForPerformer();
    List<Task> getTasksForAuthor();

    Task updateTask(UpdateTaskRequest request);
    Task updateTaskStatus(Long taskId, TaskStatus status);

    void deleteTask(Long taskId);
}
