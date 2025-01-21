package com.example.taskmanagementservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TaskManagementServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(TaskManagementServiceApplication.class, args);
		log.warn("APP APP APPLICATION STARTED");
	}

}
