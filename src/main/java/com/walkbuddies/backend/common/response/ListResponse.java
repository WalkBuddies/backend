package com.walkbuddies.backend.common.response;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ListResponse<T> {
    int statusCode;
    String message;
    List<T> data;
}