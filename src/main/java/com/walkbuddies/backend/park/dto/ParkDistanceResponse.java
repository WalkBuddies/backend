package com.walkbuddies.backend.park.dto;

import com.walkbuddies.backend.park.domain.ParkEntity;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkDistanceResponse {
    private Long parkId;
    private String parkName;
    private Double distance;

    public static ParkDistanceResponse convertToDto(ParkEntity parkEntity) {
        return ParkDistanceResponse.builder()
                .parkId(parkEntity.getParkId())
                .parkName(parkEntity.getParkName())
                .build();
    }
}
