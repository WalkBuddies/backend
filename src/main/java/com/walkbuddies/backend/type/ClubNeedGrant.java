package com.walkbuddies.backend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClubNeedGrant {

    GRANT_NEED(1),

    GRANT_NOT_NEED(2);

    private final Integer value;
}
