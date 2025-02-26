package com.example.taskmanagementservice.user.dto;

import com.example.taskmanagementservice.user.model.Role;
import jakarta.persistence.*;
import lombok.*;

@Value
@Builder
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String email;

    Role role;
}