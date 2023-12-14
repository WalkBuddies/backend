package com.walkbuddies.backend.dto.clubservice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ClubResponse {
    private final int statusCode;
    private final String message;
    private final ClubDto data;
}