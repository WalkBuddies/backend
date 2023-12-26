package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "my_club")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyClubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myClubId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberId;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private ClubEntity clubId;

    private Integer authority;
    private LocalDate regDate;
}
