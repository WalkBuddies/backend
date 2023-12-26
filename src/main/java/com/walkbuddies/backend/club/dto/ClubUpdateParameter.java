package com.walkbuddies.backend.club.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubUpdateParameter {
    private Long clubId;
    private String clubName;
    private Long townId;
    private Long ownerId;
    private Integer members;
    private Integer membersLimit;
    private Integer accessLimit;
    private Integer needGrant;
    private Boolean isSuspended;
    private LocalDate regDate;
    private LocalDate modDate;

    private Long memberId;
}
