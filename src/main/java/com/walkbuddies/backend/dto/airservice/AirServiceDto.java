package com.walkbuddies.backend.dto.airservice;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirServiceDto {

  private String stationName;
  private String sidoName;
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
