package com.example.taskmanagementservice.auth.request;


import com.example.taskmanagementservice.user.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegistrationResponse {

    private Integer id;

    @Schema(description = "Почта", example = "email@example.ex")
    private String email;

    private Role role;
}
