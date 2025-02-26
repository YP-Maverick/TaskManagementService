package com.example.taskmanagementservice.task.controller;

import com.example.taskmanagementservice.task.model.TaskPriority;
import com.example.taskmanagementservice.task.model.TaskStatus;
import com.example.taskmanagementservice.task.request.CreateTaskRequest;
import com.example.taskmanagementservice.task.model.TaskMapper;
import com.example.taskmanagementservice.task.dto.TaskDto;
import com.example.taskmanagementservice.task.model.Task;
import com.example.taskmanagementservice.task.request.UpdateTaskRequest;
import com.example.taskmanagementservice.task.request.UpdateTaskStatusRequest;
import com.example.taskmanagementservice.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.createTask(request)
                )
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<TaskDto> updateTask(
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.updateTask(request)
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/status")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @RequestBody @Valid UpdateTaskStatusRequest request
    ) {
        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.updateTaskStatus(request.getTaskId(), request.getStatus())
                )
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId)
    {
        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.getTaskById(taskId)
                )
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sort[0])
        );

        // Получаем отфильтрованные данные
        Page<Task> tasks = taskService.getAllTasks(status, priority, pageable);

        // Конвертируем в DTO
        return ResponseEntity.ok(tasks.map(taskMapper::toDto));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/performer")
    public ResponseEntity<List<TaskDto>> getTasksForPerformer() {
        return ResponseEntity.ok(
                taskMapper.toDtoList(
                        taskService.getTasksForPerformer()
                )
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/author")
    public ResponseEntity<List<TaskDto>> getTasksForAuthor() {
        return ResponseEntity.ok(
                taskMapper.toDtoList(
                        taskService.getTasksForAuthor()
                )
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

 }
