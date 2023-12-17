package com.walkbuddies.backend.exception.impl;

import com.walkbuddies.backend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyMemberException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 가입한 소모임에는 가입 할 수 없습니다.";
    }
}
