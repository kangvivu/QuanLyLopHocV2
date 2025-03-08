package com.example.demotrungtam.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomNotFoundException extends RuntimeException {
    private String code;

    public CustomNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}
