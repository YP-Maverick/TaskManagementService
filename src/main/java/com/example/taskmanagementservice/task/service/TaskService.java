package com.example.taskmanagementservice.task.service;

import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskStatus;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.request.UpdateTaskRequest;

import java.util.List;

public interface TaskService {

    Task createTask(CreateTaskRequest request);

    Task updateTask(Long taskId, UpdateTaskRequest request);

    Task updateTaskStatus(Long taskId, TaskStatus status);

    Task getTaskById(Long taskId);

    List<Task> getAllTasks();

    List<Task> getTasksForPerformer();

    List<Task> getTasksForAuthor();

    void deleteTask(Long taskId);

}
