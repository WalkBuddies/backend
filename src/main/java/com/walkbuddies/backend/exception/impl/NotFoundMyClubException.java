package com.walkbuddies.backend.exception.impl;

import com.walkbuddies.backend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFoundMyClubException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return "해당 유저는 소모임의 멤버가 아닙니다.";
    }
}
