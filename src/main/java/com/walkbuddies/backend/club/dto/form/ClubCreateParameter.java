package com.walkbuddies.backend.club.dto.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubCreateParameter {

    private String clubName;
    private long townId;
    private long ownerId;
    private int membersLimit;
    private int accessLimit;
    private int needGrant;
}
