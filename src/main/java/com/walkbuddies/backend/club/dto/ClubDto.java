package com.walkbuddies.backend.club.dto;

import com.walkbuddies.backend.club.domain.ClubEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubDto {

    private Long clubId;
    private String clubName;
    private Long townId;
    private Long ownerId;
    private Integer members;
    private Integer membersLimit;
    private Integer accessLimit;
    private Integer needGrant;
    private LocalDate regDate;
    private LocalDate modDate;

    public static ClubResponse of(int statusCode, String message, ClubEntity clubEntity) {

        ClubDto clubData = ClubDto.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .townId(clubEntity.getTownId().getTownId())
                .ownerId(clubEntity.getOwnerId().getMemberId())
                .members(clubEntity.getMembers())
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(clubEntity.getModDate())
                .build();

        return new ClubResponse(statusCode, message, clubData);
    }
}