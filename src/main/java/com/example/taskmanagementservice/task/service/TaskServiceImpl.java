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

    private void validateTaskOwnership(Task task) {
        Integer currentUserId = getCurrentUser().getId();
        Integer authorId = task.getAuthor().getId();
        Integer performerId = task.getPerformer().getId();

        if (!currentUserId.equals(authorId) && !currentUserId.equals(performerId)) {
            throw new AuthenticationException("Only task author or performer can update the status");
        }
    }

    @Override
    public Task createTask(CreateTaskRequest request) {
        User author = getCurrentUser();
        checkUserByEmail(author.getEmail());

        User performer = findPerformerById(request.getPerformerId());


        Task createdTask = taskRepository.save(
                taskMapper.toTask(
                        request,
                        author,
                        performer));

        log.info("Successfully createTask with taskId={} by userId={}, email:{}",
                createdTask.getId(),
                author.getId(),
                author.getEmail()
        );
        return createdTask;
    }

    @Override
    public Task updateTask(UpdateTaskRequest request) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        User performer = findPerformerById(request.getPerformerId());

        Task existingTask = findTaskById(request.getTaskId());

        validateAuthorEmail(existingTask.getAuthor().getEmail(), requester.getEmail());

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setPriority(request.getPriority());
        existingTask.setStatus(request.getStatus());
        existingTask.setPerformer(performer);

        Task updatedTask = taskRepository.save(existingTask);

        log.info("Successfully updateTask with taskId={} by userId={} email:{}",
                updatedTask.getId(),
                requester.getId(),
                requester.getEmail()
        );
        return updatedTask;
    }

    @Override
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());;

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task with id " + taskId + " not found."));

        validateTaskOwnership(task);

        task.setStatus(status);

        log.info("Successfully updateTaskStatus with taskId={} by userId={}, email:{}",
                taskId,
                requester.getId(),
                requester.getEmail()
        );
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(Long taskId) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException(
                        "Task with id " + taskId + " not found.")
        );

        log.info("Successfully getTaskById with taskId={} by userId={}, email:{}",
                taskId,
                requester.getId(),
                requester.getEmail()
        );
        return task;
    }

    @Override
    public Page<Task> getAllTasks(TaskStatus status, TaskPriority priority, Pageable pageable) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        Specification<Task> spec = (root, query, cb) -> null;
        spec = spec.and(TaskSpecifications.withStatus(status))
                .and(TaskSpecifications.withPriority(priority));

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        log.info("Successfully getAllTasks by userId={}, email:{}",
                requester.getId(), requester.getEmail()
        );
        return taskPage;
    }

    @Override
    public List<Task> getTasksForAuthor() {
        User requester = getCurrentUser();

        List<Task> tasks = taskRepository.findByAuthor(requester);

        log.info("Successfully getTasksForAuthor by userId={}, email:{}",
                requester.getId(), requester.getEmail()
        );
        return tasks;
    }

    public List<Task> getTasksForPerformer() {
        User requester = getCurrentUser();

        List<Task> tasks = taskRepository.findByPerformer(requester);

        log.info("Successfully getTasksForPerformer by userId={}, email:{}",
                requester.getId(), requester.getEmail()
        );
        return tasks;
    }

    @Override
    public void deleteTask(Long taskId) {
        User requester = getCurrentUser();
        checkUserByEmail(requester.getEmail());

        Task existingTask = findTaskById(taskId);

        validateAuthorEmail(existingTask.getAuthor().getEmail(), requester.getEmail());
        taskRepository.delete(getTaskById(taskId));

        log.info("Successfully deleteTask with taskId={} by userId={}, email:{}",
                taskId, requester.getId(), requester.getEmail()
        );
    }
}
