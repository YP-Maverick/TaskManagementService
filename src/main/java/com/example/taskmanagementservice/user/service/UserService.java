package com.example.taskmanagementservice.user.service;

import com.example.taskmanagementservice.user.model.Role;
import com.example.taskmanagementservice.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean isAdmin(User user);

    User createUser(User user);

    User getUserByEmail(String email);
    User getCurrentUser();
    Page<User> getAllUsers(Pageable pageable, Role role);

    void deleteUser(Integer userId);
}
