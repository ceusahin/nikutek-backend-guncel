package com.example.nikutek.exception;

import org.springframework.http.HttpStatus;

public class NikutekException extends RuntimeException {
    private HttpStatus status;

    public NikutekException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
