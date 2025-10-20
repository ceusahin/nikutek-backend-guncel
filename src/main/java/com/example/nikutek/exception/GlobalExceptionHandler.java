package com.example.nikutek.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<NikutekErrorResponse> handleException(NikutekException nikutekException) {
        NikutekErrorResponse nikutekErrorResponse = new NikutekErrorResponse(
                nikutekException.getMessage(),
                nikutekException.getStatus().value(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(nikutekErrorResponse, nikutekException.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<NikutekErrorResponse> handleException(Exception exception) {
        NikutekErrorResponse nikutekErrorResponse = new NikutekErrorResponse(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(nikutekErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}