package com.walkbuddies.backend.air.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirServiceDto {

    private String stationName;
    private LocalDateTime dataTime;
    private Float so2Value;
    private Float coValue;
    private Float o3Value;
    private Float no2Value;
    private int pm10Value;
    private int pm25Value;
    private int khaiValue;
    private int khaiGrade;
    private int so2Grade;
    private int coGrade;
    private int o3Grade;
    private int pm10Grade;
    private int pm25Grade;
    private int stationCode;

}
