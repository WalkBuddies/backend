package com.walkbuddies.backend.dto.clubservice;

import com.walkbuddies.backend.domain.clubservice.MyClubEntity;
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
}
