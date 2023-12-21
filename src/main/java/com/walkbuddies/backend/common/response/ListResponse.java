package com.walkbuddies.backend.common.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> {
    int statusCode;
    String message;
    List<T> data;
}