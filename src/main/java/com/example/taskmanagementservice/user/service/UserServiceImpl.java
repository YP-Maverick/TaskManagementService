package com.example.taskmanagementservice.user.service;

import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.DuplicateException;
import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.jwt.service.JWTService;
import com.example.taskmanagementservice.user.model.Role;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JWTService jwtService;

    private final UserRepository userRepository;

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isAdmin(User user) {
        return userRepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException("User not found")
        ).getRole().equals(Role.ROLE_ADMIN);
    }

    /**
     * Проверяет, может ли текущий пользователь удалить указанного пользователя.
     * Удаление разрешено, если:
     * 1. Текущий пользователь — администратор.
     * 2. Текущий пользователь удаляет себя.
     */
    private boolean canDeleteUser(Integer userId) {
        User currentUser = getCurrentUser();
        return currentUser.getRole().equals(Role.ROLE_ADMIN)
                || currentUser.getId().equals(userId);
    }

    @Override
    public User createUser(User user) {

        try {
            User createdUser = userRepository.save(user);
            log.info("Successfully createdUser with userId={}, email:{}",
                    createdUser.getId(), createdUser.getEmail()
            );
            return createdUser;
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate. Request to create with already used email address for another user {}", user.getEmail());
            throw new DuplicateException("This email is already in use.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        log.info("Successfully loadUserByUsername with userId={}, email:{}",
                user.getId(), user.getEmail()
        );
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        log.info("Successfully getUserByEmail with userId={}, email:{}",
                user.getId(), user.getEmail()
        );
        return user;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable, Role role) {
        log.info("Successfully getUserByEmail with pageable={}, role:{}",
                pageable, role
        );

        if (role != null) {
            return userRepository.findByRole(role, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!canDeleteUser(userId)) {
            throw new AuthenticationException("You do not have permission to delete this user.");
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
             new NotFoundException("User with id " + userId + " not found.")
        );

        jwtService.revokeAllUserRefreshTokens(user);
        userRepository.deleteById(userId);
        log.info("Successfully deleteUser with userId={}, email:{}",
                userId, user.getEmail()
        );
    }
}
