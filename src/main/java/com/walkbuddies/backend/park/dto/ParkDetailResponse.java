package com.walkbuddies.backend.park.dto;

import com.walkbuddies.backend.park.domain.ParkEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkDetailResponse {
    private Long parkId;
    private String parkName;
    private String latitude;
    private String longitude;
    private String address;
    private String sportFacility;
    private String convenienceFacility;

    public static ParkDetailResponse convertToDto(ParkEntity parkEntity) {
        return ParkDetailResponse.builder()
                .parkId(parkEntity.getParkId())
                .parkName(parkEntity.getParkName())
                .latitude(String.valueOf(parkEntity.getLatitude()))
                .longitude(String.valueOf(parkEntity.getLongitude()))
                .address(parkEntity.getAddress())
                .sportFacility(parkEntity.getSportFacility())
                .convenienceFacility(parkEntity.getConvenienceFacility())
                .build();
    }
}
