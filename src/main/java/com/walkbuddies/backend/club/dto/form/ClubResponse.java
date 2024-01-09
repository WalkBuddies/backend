package com.walkbuddies.backend.club.dto.form;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.dto.ClubDto;
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
    private String ownerNickName;
    private int members;
    private int membersLimit;
    private int accessLimit;
    private int needGrant;
    private boolean isSuspended;
    private LocalDateTime regDate;

    public static ClubResponse of(ClubEntity clubEntity) {

        return ClubResponse.builder()
                .clubName(clubEntity.getClubName())
                .townName(clubEntity.getTown().getTownName())
                .ownerNickName(clubEntity.getOwner().getNickname())
                .members(clubEntity.getMembers())
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .isSuspended(clubEntity.isSuspended())
                .regDate(clubEntity.getRegDate())
                .build();
    }

}
