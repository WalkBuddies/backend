package com.walkbuddies.backend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClubAccessLimit {

    SEARCH_ALLOW(1),

    SEARCH_NOT_ALLOW(2);

    private final Integer value;
}
