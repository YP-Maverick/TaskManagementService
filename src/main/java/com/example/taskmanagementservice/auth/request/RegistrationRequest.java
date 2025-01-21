package com.example.taskmanagementservice.auth.request;

import com.example.taskmanagementservice.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegistrationRequest {

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,32}$",
            message = "Пароль должен содержать от 8 до 32 символов, буквы и цифры.")
    String password;

    Role role;
}
