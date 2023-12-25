package com.walkbuddies.backend.club.dto;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.repository.ClubRepository;
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
    private Long ownerId;
    private Integer members;
    private Integer membersLimit;
    private Integer accessLimit;
    private Integer needGrant;
    private LocalDate regDate;
    private LocalDate modDate;
    private List<String> waitingMembers;
}