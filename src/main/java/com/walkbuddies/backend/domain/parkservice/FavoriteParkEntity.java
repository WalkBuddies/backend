package com.walkbuddies.backend.domain.parkservice;

import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

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
