package com.example.taskmanagementservice.task.service;

import com.example.taskmanagementservice.task.model.Task;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    Task updateTask(Task task);

    Task getTaskById(Long taskId);

    List<Task> getAllTasks();

    List<Task> getTasksForCurrentPerformer();

    void deleteTask(Long taskId);

}
