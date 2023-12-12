package com.walkbuddies.backend.dto.clubservicec;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ClubResponse {
    private final int statusCode;
    private final String message;
    private final ClubDto data;
}
