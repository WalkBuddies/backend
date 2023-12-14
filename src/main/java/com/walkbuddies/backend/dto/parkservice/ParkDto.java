package com.walkbuddies.backend.dto.parkservice;

import com.walkbuddies.backend.domain.parkservice.ParkEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkDto {
    private String parkName;
    private String latitude;
    private String longitude;
    private String address;
    private String sportFacility;
    private String convenienceFacility;

    private float distance;

    public static ParkDto convertToDto(ParkEntity parkEntity) {
        return ParkDto.builder()
                .parkName(parkEntity.getParkName())
                .latitude(String.valueOf(parkEntity.getLatitude()))
                .longitude(String.valueOf(parkEntity.getLongitude()))
                .address(parkEntity.getAddress())
                .sportFacility(parkEntity.getSportFacility())
                .convenienceFacility(parkEntity.getConvenienceFacility())
                .build();
    }
}
