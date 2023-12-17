package com.walkbuddies.backend.exception.impl;

import com.walkbuddies.backend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotMyTownException extends AbstractException {


    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "동네 인증을 먼저 해주세요.";
    }
}
