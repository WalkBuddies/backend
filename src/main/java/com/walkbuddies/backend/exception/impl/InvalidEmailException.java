package com.walkbuddies.backend.exception.impl;

import com.walkbuddies.backend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class InvalidEmailException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이메일 형식이 아닙니다.";
    }
}
