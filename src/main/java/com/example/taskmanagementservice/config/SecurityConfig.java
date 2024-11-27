package com.example.taskmanagementservice.config;

import com.example.taskmanagementservice.jwt.service.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v1/api-docs",
            "/v1/api-docs",
            "/v1/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF (для REST API)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll() // Публичные URIs

                        .requestMatchers(GET, "/api/v1/tasks/{taskId}/performer").hasRole("USER")
                        .requestMatchers(PUT, "/api/v1/tasks/{taskId}/status").hasRole("USER")

                        .requestMatchers(POST, "/api/v1/tasks/**").hasRole("ADMIN")
                        .requestMatchers(PUT, "/api/v1/tasks/**").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/api/v1/tasks/**").hasRole("ADMIN")
                        .requestMatchers(GET, "/api/v1/tasks/**").hasRole("ADMIN")

                        .requestMatchers(POST, "/api/v1/comments/**").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/api/v1/comments/**").hasRole("ADMIN")
                        .requestMatchers(GET, "/api/v1/comments/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // Настройка сессии: Stateless (для токенов JWT)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)

                // Добавляем фильтр перед UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                    logout.logoutUrl("/api/v1/auth/logout")
                            .addLogoutHandler(logoutHandler)
                            .logoutSuccessHandler((request, response, authentication)
                                    -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}