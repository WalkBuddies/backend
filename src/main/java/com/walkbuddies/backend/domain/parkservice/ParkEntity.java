package com.walkbuddies.backend.domain.parkservice;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "park")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "park_id")
    private long parkId;

    @Column(name = "park_name")
    private String parkName;
    @Column(name = "longitude")
    private float longitude;
    @Column(name = "latitude")
    private float latitude;
    @Column(name = "address")
    private String address;
}
