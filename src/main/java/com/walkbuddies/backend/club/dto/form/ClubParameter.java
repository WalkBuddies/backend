package com.walkbuddies.backend.club.dto.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubParameter {

    private long clubId;
    private long memberId;
    private long ownerId;
    private long townId;
    private String clubName;
}
