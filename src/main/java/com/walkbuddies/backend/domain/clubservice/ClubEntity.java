package com.walkbuddies.backend.domain.clubservice;

import com.walkbuddies.backend.domain.memberservice.MemberEntity;
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
    private MemberEntity townId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity ownerId;

    private Integer members;
    private Integer membersLimit;
    private Integer accessLimit;
    private Integer needGrant;
    private LocalDate regDate;
    private LocalDate modDate;

}
