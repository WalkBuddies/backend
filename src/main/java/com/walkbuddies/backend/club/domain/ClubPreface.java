package com.walkbuddies.backend.club.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club_preface")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubPreface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prefaceId;

    private String preface;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private ClubEntity clubId;
}
