package com.example.taskmanagementservice;

import com.example.taskmanagementservice.auth.request.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ValueTest {



    @Test
    void testExample() {

        RegistrationRequest request = RegistrationRequest.builder()
                .email("ooo@gmail.com")
                .password("4444pass")
                .build();

        log.info("Request: {}", request);
    }

}