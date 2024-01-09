package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "town_id")
    private TownEntity town;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity owner;

    private Integer members;
    private Integer membersLimit;
    private Integer accessLimit;
    private Integer needGrant;
    private boolean isSuspended;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}