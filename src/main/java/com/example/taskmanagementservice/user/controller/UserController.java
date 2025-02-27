package com.example.taskmanagementservice.user.controller;

import com.example.taskmanagementservice.task.dto.TaskDto;
import com.example.taskmanagementservice.user.dto.UserDto;
import com.example.taskmanagementservice.user.model.Role;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.model.UserMapper;
import com.example.taskmanagementservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "User", description = "Управление пользователями")

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @Operation(
            summary = "Получение всех пользователей",
            description = "Позволяет получить всех пользователей с пагинацией и сортировкой по id." +
                    " Доступно только для администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задачи успешно получены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @RequestParam(required = false) Role role) { // Добавлен параметр role

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sort[0])
        );

        Page<User> users = userService.getAllUsers(pageable, role);

        return ResponseEntity.ok(users.map(userMapper::toDto));
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Позволяет удалить пользователя по id." +
                          " Доступно только для самого пользователя или администраторов.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Необходима аутентификация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
