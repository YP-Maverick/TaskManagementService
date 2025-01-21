package com.example.taskmanagementservice.task.controller;

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
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.updateTask(taskId, request)
                )
        );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody @Valid UpdateTaskStatusRequest request
    ) {
        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.updateTaskStatus(taskId, request.getStatus())
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
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(List.of());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/me/performer")
    public ResponseEntity<List<TaskDto>> getTasksForPerformer() {
        return ResponseEntity.ok(
                taskMapper.toDtoList(
                        taskService.getTasksForPerformer()
                )
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/me/author")
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
