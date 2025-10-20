package com.example.nikutek.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends NikutekException{
    public NotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}