package com.example.taskmanagementservice.exception;

public class InvalidTokenFormatException extends RuntimeException {

    public InvalidTokenFormatException(String message) {
        super(message);
    }
}
