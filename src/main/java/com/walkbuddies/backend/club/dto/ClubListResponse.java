package com.walkbuddies.backend.club.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ClubListResponse {
    private final int statusCode;
    private final String message;
    private final List<ClubDto> data;
}