package com.walkbuddies.backend.club.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubJoinInform {

    private Long memberId;
    private Long clubId;
    private Long townId;
    private String message;
    private Boolean allowJoin;
}
