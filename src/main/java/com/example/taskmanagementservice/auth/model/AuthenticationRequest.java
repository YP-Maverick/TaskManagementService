package com.example.taskmanagementservice.auth.model;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
public class AuthenticationRequest {

    String email;
    String password;
}