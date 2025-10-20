package com.example.nikutek.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NikutekErrorResponse {
    private String message;
    private int status;
    private long timestamp;
}