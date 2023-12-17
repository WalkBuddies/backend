package com.walkbuddies.backend.domain.clubservice;

import com.walkbuddies.backend.domain.memberservice.MemberEntity;
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
    @JoinColumn(name = "memberId")
    private MemberEntity memberId;

    @ManyToOne
    @JoinColumn(name = "clubId")
    private ClubEntity clubId;

    private Integer authority;
    private LocalDate regAt;
}
