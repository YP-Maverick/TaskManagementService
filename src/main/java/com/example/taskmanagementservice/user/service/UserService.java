package com.example.taskmanagementservice.user.service;

import com.example.taskmanagementservice.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User createUser(User user);
}
