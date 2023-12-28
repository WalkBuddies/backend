package com.walkbuddies.backend.exception.impl;

import com.walkbuddies.backend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFoundPrefaceException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.NOT_FOUND.value();
  }

  @Override
  public String getMessage() {
    return "말머리가 없습니다.";
  }
}
