package com.example.taskmanagementservice.user.service;

import com.example.taskmanagementservice.exception.DuplicateException;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        log.info("Request to create or update a user.");

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate. Request to create with already used email address for another user {}", user.getEmail());
            throw new DuplicateException("This email is already in use.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found")
        );
    }

}
