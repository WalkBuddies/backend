package com.walkbuddies.backend.dto.parkservice;

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

    private float distance;
}
