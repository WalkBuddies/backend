package com.walkbuddies.backend.exception.impl;

import com.walkbuddies.backend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ChangeClubOwnerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "소모임 장은 소모임 탈퇴가 불가능 합니다. 소모임 장을 변경한 후 탈퇴 해주세요.";
    }
}
