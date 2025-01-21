package com.example.taskmanagementservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {
/*
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService(); // Создаем экземпляр сервиса
    }

    @Test
    void testGenerateToken() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("user@example.com");

        String token = jwtService.generateToken(mockUserDetails);
        assertNotNull(token);

        // Проверяем, что токен содержит ожидаемые данные
        String extractedEmail = jwtService.extractEmail(token);
        assertEquals("user@example.com", extractedEmail);
    }

    @Test
    void testValidateToken() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("user@example.com");

        String token = jwtService.generateToken(mockUserDetails);

        // Проверяем, что токен валиден
        assertTrue(jwtService.isTokenValid(token, mockUserDetails));

        // Проверяем, что неверный UserDetails не пройдёт валидацию
        when(mockUserDetails.getUsername()).thenReturn("invalid@example.com");
        assertFalse(jwtService.isTokenValid(token, mockUserDetails));
    }

    @Test
    void testIsTokenExpired() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("user@example.com");

        String token = jwtService.generateToken(mockUserDetails);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void testExtractClaim() {
        User mockUserDetails = mock(User.class);
        when(mockUserDetails.getUsername()).thenReturn("user@example.com");

        String token = jwtService.generateToken(mockUserDetails);

        String string = jwtService.extractEmail(token);
        assertEquals("user@example.com", string);
    }*/
}

