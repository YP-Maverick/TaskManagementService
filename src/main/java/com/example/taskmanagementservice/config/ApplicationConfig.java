package com.example.taskmanagementservice.config;

import com.example.taskmanagementservice.exception.EmailNotFoundException;
import com.example.taskmanagementservice.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class ApplicationConfig {

    private UserRepository userRepository;


    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("saf"));
    }

}
