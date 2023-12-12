package com.walkbuddies.backend.dto.clubservicec;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.type.ClubRole;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubDto {

    private Long clubId;
    private String clubName;
    private Long townId;
    private Long memberId;
    private ClubRole clubRole;
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
                .townId(clubEntity.getTownId())
                .memberId(clubEntity.getMemberId())
                .clubRole(clubEntity.getClubRole())
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
