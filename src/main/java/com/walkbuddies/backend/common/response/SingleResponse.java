package com.walkbuddies.backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SingleResponse <T> {
    int statusCode;
    String message;
    T data;
}