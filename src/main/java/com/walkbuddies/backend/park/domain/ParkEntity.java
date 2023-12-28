package com.walkbuddies.backend.park.domain;

import com.walkbuddies.backend.park.dto.ParkRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "park")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "park_id")
    private Long parkId;

    @Column(name = "park_name")
    private String parkName;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "address")
    private String address;
    @Column(name = "sport_facility")
    private String sportFacility;
    @Column(name = "convenience_facility")
    private String convenienceFacility;

    public static ParkEntity convertToEntity(ParkRequest parkRequest) {
        return ParkEntity.builder()
                .parkName(parkRequest.getParkName())
                .longitude(Double.valueOf(parkRequest.getLongitude()))
                .latitude(Double.valueOf(parkRequest.getLatitude()))
                .address(parkRequest.getAddress())
                .sportFacility(parkRequest.getSportFacility())
                .convenienceFacility(parkRequest.getConvenienceFacility())
                .build();
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setSportFacility(String sportFacility) {
        this.sportFacility = sportFacility;
    }

    public void setConvenienceFacility(String convenienceFacility) {
        this.convenienceFacility = convenienceFacility;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
