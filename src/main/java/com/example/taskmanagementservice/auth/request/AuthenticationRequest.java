package com.example.taskmanagementservice.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Value
@Builder
public class AuthenticationRequest {

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Pattern(
    regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,32}$",
    message = "Пароль должен содержать от 8 до 32 символов, буквы и цифры.")
    String password;
}