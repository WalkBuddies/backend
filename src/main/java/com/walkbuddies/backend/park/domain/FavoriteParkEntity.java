package com.walkbuddies.backend.park.domain;

import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_park")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteParkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_park_id", nullable = false, unique = true)
    private Long favoriteParkId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "park_id")
    private ParkEntity park;

    public FavoriteParkEntity(MemberEntity member, ParkEntity park) {
        this.member = member;
        this.park = park;
    }
}
