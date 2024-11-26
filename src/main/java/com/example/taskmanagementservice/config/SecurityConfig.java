package com.example.taskmanagementservice.config;

import com.example.taskmanagementservice.jwt.service.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthFilter; // Подключаем JWT фильтр
    private final UserDetailsService userDetailsService; // Сервис для работы с UserDetails

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF (для REST API)
                .csrf(csrf -> csrf.disable())

                // Настройка авторизаций запросов
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll() // Публичные URIs

                        .requestMatchers(POST, "/api/v1/tasks/**").hasRole("ADMIN") // Управление задачами (создание)
                        .requestMatchers(PUT, "/api/v1/tasks/**").hasRole("ADMIN") // Управление задачами (редактирование)
                        .requestMatchers(DELETE, "/api/v1/tasks/**").hasRole("ADMIN") // Управление задачами (удаление)
                        .requestMatchers(GET, "/api/v1/tasks/**").hasRole("ADMIN") // TODO

                        .requestMatchers(POST, "/api/v1/comments/**").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/api/v1/comments/**").hasRole("ADMIN")
                        .requestMatchers(GET, "/api/v1/comments/**").hasRole("ADMIN")
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )

                // Настройка сессии: Stateless (для токенов JWT)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Добавляем фильтр перед UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            // Реализация обработки выхода пользователя из системы (logout)
            // Здесь вы можете выполнить такие действия, как сброс токена и очистка безопасности
            response.setStatus(200);
        };
    }
}