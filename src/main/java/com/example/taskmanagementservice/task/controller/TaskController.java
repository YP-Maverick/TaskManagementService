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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task", description = "Управление задачами")

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;


    @Operation(
            summary = "Создание новой задачи",
            description = "Создает новую задачу на основе предоставленных данных. Доступно только для администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Задача успешно создана",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        log.info("Received createTask request. request={}", request);

        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.createTask(request)
                )
        );
    }

    @Operation(
            summary = "Обновление задачи",
            description = "Позволяет обновить поля задачи кроме id автора. Доступно только для автора.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Задача успешно обновлена",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<TaskDto> updateTask(
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        log.info("Received updateTask request. request={}", request);

        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.updateTask(request)
                )
        );
    }


    @Operation(
            summary = "Обновление статуса задачи",
            description = "Позволяет обновить статус задачи по id" +
                    " Доступно только для авторов и исполнителей.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Задача успешно обновлена",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/status")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @RequestBody @Valid UpdateTaskStatusRequest request
    ) {
        log.info("Received updateTaskStatus. request={}", request);

        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.updateTaskStatus(request.getTaskId(), request.getStatus())
                )
        );
    }

    @Operation(
            summary = "Получение задачи",
            description = "Позволяет получить задачу по id" +
                    " Доступно только для администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Задача успешно получена",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId)
    {
        log.info("Received getTaskById. taskId={}", taskId);

        return ResponseEntity.ok(
                taskMapper.toDto(
                        taskService.getTaskById(taskId)
                )
        );
    }

    //TODO Page<TaskDto>
    @Operation(
            summary = "Получение всех задач",
            description = "Позволяет получить все задачи с пагинацией и сортировкой по статусу и приоритету." +
                    " Доступно только для администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Задачи успешно получены",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        log.info("Received getAllTasks. status={}, priority={}, page={}, size={}, sort={} ",
                status, priority, page, size, sort
        );

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sort[0])
        );

        Page<Task> tasks = taskService.getAllTasks(status, priority, pageable);

        return ResponseEntity.ok(tasks.map(taskMapper::toDto));
    }


    @Operation(
            summary = "Получение задач исполнителем",
            description = "Позволяет получить задачи, для которых текущий пользователь является исполнителем." +
                    " Доступно только для любых авторизованных пользователей.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/performer")
    public ResponseEntity<List<TaskDto>> getTasksForPerformer() {
        log.info("Received getTasksForPerformer.");

        return ResponseEntity.ok(
                taskMapper.toDtoList(
                        taskService.getTasksForPerformer()
                )
        );
    }


    //TODO List<TaskDto>
    @Operation(
            summary = "Получение задач автором",
            description = "Позволяет получить задачи, автором которых является текущий пользователь." +
                          " Доступно только для администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Задачи успешно получены",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/author")
    public ResponseEntity<List<TaskDto>> getTasksForAuthor() {
        log.info("Received getTasksForAuthor().");

        return ResponseEntity.ok(
                taskMapper.toDtoList(
                        taskService.getTasksForAuthor()
                )
        );
    }

    @Operation(
            summary = "Удаление задачи",
            description = "Позволяет удалить задачу по id. Доступно только для автора.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Задача успешно удалена",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        log.info("Received deleteTask. taskId={}", taskId);

        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

 }
