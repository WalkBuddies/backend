package com.walkbuddies.backend.park.dto;

import com.walkbuddies.backend.park.domain.FavoriteParkEntity;
import com.walkbuddies.backend.park.domain.ParkEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkResponse {
    private Long parkId;
    private String parkName;

    public static ParkResponse convertToDto(ParkEntity parkEntity) {
        return ParkResponse.builder()
                .parkId(parkEntity.getParkId())
                .parkName(parkEntity.getParkName())
                .build();
    }

    public static ParkResponse convertFavoriteToDto(FavoriteParkEntity favoritePark) {
        ParkEntity park = favoritePark.getPark();
        return ParkResponse.builder()
                .parkId(park.getParkId())
                .parkName(park.getParkName())
                .build();
    }
}
