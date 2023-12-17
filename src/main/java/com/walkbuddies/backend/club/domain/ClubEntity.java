package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "club")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    private String clubName;

    @ManyToOne
    @JoinColumn(name = "townId")
    private TownEntity townId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity ownerId;

    private Integer members;
    private Integer membersLimit;
    private Integer accessLimit;
    private Integer needGrant;
    private LocalDate regDate;
    private LocalDate modDate;

    public static ClubDto entityToDto(ClubEntity clubEntity) {

        return ClubDto.builder()
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
    }

}