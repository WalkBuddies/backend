package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club_wating")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubWaitingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingId;

    @ManyToOne
    @JoinColumn(name = "clubId")
    private ClubEntity clubId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity memberId;

    @ManyToOne
    @JoinColumn(name = "nickname")
    private MemberEntity nickName;

    private String message;
}
