package com.example.taskmanagementservice.comment.controller;

import com.example.taskmanagementservice.comment.dto.CommentDto;
import com.example.taskmanagementservice.comment.dto.CreateCommentRequest;
import com.example.taskmanagementservice.comment.dto.UpdateCommentRequest;
import com.example.taskmanagementservice.comment.model.CommentMapper;
import com.example.taskmanagementservice.comment.service.CommentService;
import com.example.taskmanagementservice.task.dto.TaskDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Tag(name = "Comment", description = "Управление комментариями")

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @Operation(
            summary = "Создание комментария",
            description = "Позволяет создать новый комментарий к задаче. Доступно для авторизованных пользователей",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Комментарий успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                        commentService.createComment(taskId, request.getContent())
                )
        );
    }


    @Operation(
            summary = "Создание ответа на комментарий",
            description = "Позволяет создать ответ на существующий комментарий. Доступно для авторизованных пользователей.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ на комментарий успешно создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
            @ApiResponse(responseCode = "404", description = "Задача или родительский комментарий не найдены")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/{taskId}/comments/{parentCommentId}/replies")
    public ResponseEntity<CommentDto> createReplyComment(
            @PathVariable Long taskId,
            @PathVariable Long parentCommentId,
            @Valid @RequestBody CreateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                    commentService.createReplyComment(
                                taskId,
                                parentCommentId,
                                request.getContent())
                )
        );
    }

    @Operation(
            summary = "Получение комментариев задачи",
            description = "Позволяет получить все комментарии для задачи по её ID. Доступно для пользователей для авторизованных пользователей.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByTaskId(
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(
                commentMapper.toDtoList(
                    commentService.getCommentsByTaskId(taskId)
                )
        );
    }


    @Operation(
            summary = "Получение комментария по ID",
            description = "Позволяет получить комментарий по его ID. Доступно для авторизованных пользователей",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Комментарий успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                        commentService.getCommentById(commentId)
                )
        );
    }


    @Operation(
            summary = "Обновление комментария",
            description = "Позволяет получить комментарий по его ID. Доступно только автору",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа"),
            @ApiResponse(responseCode = "404", description = "Комментарий не найден")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request
    ) {

        return ResponseEntity.ok(
                commentMapper.toDto(
                        commentService.updateComment(commentId, request.getNewContent())
                )
        );
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Позволяет комментария задачу по id. Доступно только для автора или администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

