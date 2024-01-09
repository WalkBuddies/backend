package com.walkbuddies.backend.club.dto.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubJoinParameter {

    private long memberId;
    private long clubId;
    private long townId;
    private String message;
    private boolean allowJoin;
}
