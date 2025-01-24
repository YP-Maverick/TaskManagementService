package com.example.taskmanagementservice.task.service;

import com.example.taskmanagementservice.exception.NotAuthorException;
import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskMapper;
import com.example.taskmanagementservice.task.model.TaskStatus;
import com.example.taskmanagementservice.task.repository.TaskRepository;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.request.UpdateTaskRequest;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkUserByEmail(String email) {
        userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    private User findPerformerById(Integer performerId) {
        return userRepository.findById(performerId).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    private Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("Task with id " + taskId + " not found.")
        );
    }

    private Task createUpdatedTask(UpdateTaskRequest request, User author, User performer, Long taskId) {
        return Task.builder()
                .id(taskId)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .author(author)
                .performer(performer)
                .build();
    }

    private void validateAuthorEmail(String taskEmail, String authorEmail) {
        if (!taskEmail.equals(authorEmail)) {
            throw new NotAuthorException("Email not match");
        }
    }

    @Override
    public Task createTask(CreateTaskRequest request) {
        log.info("createTask");

        User author = getCurrentUser();
        checkUserByEmail(author.getEmail());

        User performer = findPerformerById(request.getPerformerId());

        return taskRepository.save(
                taskMapper.toTask(
                        request,
                        author,
                        performer));
    }

    @Override
    public Task updateTask(Long taskId, UpdateTaskRequest request) {

        User author = getCurrentUser();
        User performer = findPerformerById(request.getPerformerId());

        Task existingTask = findTaskById(taskId);
        // TODO Выброс 404

        validateAuthorEmail(existingTask.getAuthor().getEmail(), author.getEmail());
        Task updatedTask = createUpdatedTask(request, author, performer, taskId);

        return taskRepository.save(updatedTask);
    }

    @Override
    public Task updateTaskStatus(Long taskId, TaskStatus status) {

        // TODO Реализовать логику
        return null;
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException(
                        "Task with id " + taskId + " not found.")
        );
    }

    @Override
    public List<Task> getAllTasks() {
        //TODO Добавить фильтрацию

        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksForAuthor() {
        User currentUser = getCurrentUser();
        return taskRepository.findByAuthor(currentUser);
    }

    public List<Task> getTasksForPerformer() {
        User currentUser = getCurrentUser();
        return taskRepository.findByPerformer(currentUser);
    }

    @Override
    public void deleteTask(Long taskId) {
        User author = getCurrentUser();
        Task existingTask = findTaskById(taskId);

        validateAuthorEmail(existingTask.getAuthor().getEmail(), author.getEmail());
        taskRepository.delete(getTaskById(taskId));
    }
}
