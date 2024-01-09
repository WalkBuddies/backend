package com.walkbuddies.backend.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClubAuthority {

    ADMIN(1),

    MEMBER(2),

    WAITING(3);

    private final Integer value;
}
