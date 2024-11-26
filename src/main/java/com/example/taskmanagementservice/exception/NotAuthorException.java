package com.example.taskmanagementservice.exception;

public class NotAuthorException extends RuntimeException {

    public NotAuthorException(String message) {
        super(message);
    }
}
