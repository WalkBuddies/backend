package com.walkbuddies.backend.club.dto;

import com.walkbuddies.backend.club.domain.ClubEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubDto {
    private long clubId;
    private String clubName;
    private long townId;
    private long ownerId;
    private int members;
    private int membersLimit;
    private int accessLimit;
    private int needGrant;
    private boolean isSuspended;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public static ClubDto of(ClubEntity clubEntity) {

        return ClubDto.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .townId(clubEntity.getTown().getTownId())
                .ownerId(clubEntity.getOwner().getMemberId())
                .members(clubEntity.getMembers())
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .isSuspended(clubEntity.isSuspended())
                .regDate(clubEntity.getRegDate())
                .modDate(clubEntity.getModDate())
                .build();
    }
}