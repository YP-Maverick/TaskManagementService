package com.example.taskmanagementservice.task.service;

import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.NotAuthorException;
import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.model.TaskMapper;
import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import com.example.taskmanagementservice.task.repository.TaskRepository;
import com.example.taskmanagementservice.task.repository.TaskSpecifications;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.request.UpdateTaskRequest;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Task updateTask(UpdateTaskRequest request) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        User performer = findPerformerById(request.getPerformerId());

        Task existingTask = findTaskById(request.getTaskId());
        validateAuthorEmail(existingTask.getAuthor().getEmail(), requester.getEmail());

        Task updatedTask = Task.builder()
                .id(existingTask.getId())
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus())
                .author(existingTask.getAuthor())
                .performer(performer)
                .build();

        return taskRepository.save(updatedTask);
    }

    @Override
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());;

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found."));

        validateTaskOwnership(task);

        task.setStatus(status);
        return taskRepository.save(task);
    }

    private void validateTaskOwnership(Task task) {
        Integer currentUserId = getCurrentUser().getId();
        Integer authorId = task.getAuthor().getId();
        Integer performerId = task.getPerformer().getId();

        if (!currentUserId.equals(authorId) && !currentUserId.equals(performerId)) {
            throw new AuthenticationException("Only task author or performer can update the status");
        }
    }

    @Override
    public Task getTaskById(Long taskId) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        return taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException(
                        "Task with id " + taskId + " not found.")
        );
    }

    @Override
    public Page<Task> getAllTasks(TaskStatus status, TaskPriority priority, Pageable pageable) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        Specification<Task> spec = (root, query, cb) -> null;
        spec = spec.and(TaskSpecifications.withStatus(status))
                .and(TaskSpecifications.withPriority(priority));

        return taskRepository.findAll(spec, pageable);
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
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        Task existingTask = findTaskById(taskId);

        validateAuthorEmail(existingTask.getAuthor().getEmail(), requester.getEmail());
        taskRepository.delete(getTaskById(taskId));
    }
}
