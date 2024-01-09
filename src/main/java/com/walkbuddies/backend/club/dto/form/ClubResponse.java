package com.walkbuddies.backend.club.dto.form;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubResponse {

    private String clubName;
    private String townName;
    private String ownerName;
    private int members;
    private int membersLimit;
    private int accessLimit;
    private int needGrant;
    private boolean isSuspended;
    private LocalDateTime regDate;

}
