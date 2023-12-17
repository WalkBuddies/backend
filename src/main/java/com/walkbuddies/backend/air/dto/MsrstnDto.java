package com.walkbuddies.backend.air.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MsrstnDto {
    private int stationCode;
    private String addr;
    private String stationName;
}
