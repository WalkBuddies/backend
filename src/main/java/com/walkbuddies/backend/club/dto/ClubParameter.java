package com.walkbuddies.backend.club.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubParameter {

    private Long clubId;
    private Long memberId;
    private Long ownerId;
    private Long townId;
    private String clubName;


}
