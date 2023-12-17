package com.walkbuddies.backend.domain.clubservice;

import com.walkbuddies.backend.domain.memberservice.MemberEntity;
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

    private String message;


}
