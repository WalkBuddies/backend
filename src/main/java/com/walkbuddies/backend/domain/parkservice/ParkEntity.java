package com.walkbuddies.backend.domain.parkservice;

import com.walkbuddies.backend.dto.parkservice.ParkDto;
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
    @Column(name = "sport_facility")
    private String sportFacility;
    @Column(name = "convenience_facility")
    private String convenienceFacility;

    public static ParkEntity convertToEntity(ParkDto parkDto) {
        return ParkEntity.builder()
                .parkName(parkDto.getParkName())
                .longitude(Float.parseFloat(parkDto.getLongitude()))
                .latitude(Float.parseFloat(parkDto.getLatitude()))
                .address(parkDto.getAddress())
                .sportFacility(parkDto.getSportFacility())
                .convenienceFacility(parkDto.getConvenienceFacility())
                .build();
    }
}
