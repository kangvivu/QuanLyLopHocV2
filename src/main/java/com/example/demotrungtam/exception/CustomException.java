/*
 * Copyright(C) 2011 Luvina Software Company
 *
 * CustomException.java, July 23, 2023 tnanh
 */

package com.example.demotrungtam.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Chứa thuộc tính của lớp CustomException
 *
 * @author tnanh
 */
@Getter
@Setter
public class CustomException extends RuntimeException {
    private String code;

    public CustomException(String message, String code) {
        super(message);
        this.code = code;
    }
}
