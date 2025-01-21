package com.example.taskmanagementservice.auth.request;


import com.example.taskmanagementservice.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegistrationResponse {

    private Integer id;
    private String email;
    private Role role;
}
