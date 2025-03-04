package com.example.taskmanagementservice.auth.request;


import com.example.taskmanagementservice.user.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class RegistrationResponse {

    Integer id;

    @Schema(description = "Email", example = "email@example.ex")
    String email;

    Role role;
}
