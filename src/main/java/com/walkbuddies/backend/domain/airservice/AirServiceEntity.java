package com.walkbuddies.backend.domain.airservice;

import com.walkbuddies.backend.dto.airservice.AirServiceDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "air_pollution_daily")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirServiceEntity {
  @Id
  private int stationCode;
  private String stationName;
  private LocalDateTime dataTime;
  private Float so2Value;
  private int so2Grade;
  private Float coValue;
  private int coGrade;
  private Float o3Value;
  private int o3Grade;
  private Float no2Value;
  private int pm10Value;
  private int pm10Grade;
  private int pm25Value;
  private int pm25Grade;
  private int khaiValue;
  private int khaiGrade;


  public AirServiceEntity dtoToEntity(AirServiceDto dto) {

    return AirServiceEntity.builder()
        .stationCode(dto.getStationCode())
        .stationName(dto.getStationName())
        .dataTime(dto.getDataTime())
        .so2Value(dto.getSo2Value())
        .so2Grade(dto.getSo2Grade())
        .coValue(dto.getCoValue())
        .coGrade(dto.getCoGrade())
        .o3Value(dto.getO3Value())
        .o3Grade(dto.getO3Grade())
        .no2Value(dto.getNo2Value())
        .pm10Value(dto.getPm10Value())
        .pm10Grade(dto.getPm10Grade())
        .pm25Value(dto.getPm25Value())
        .pm25Grade(dto.getPm25Grade())
        .khaiValue(dto.getKhaiValue())
        .khaiGrade(dto.getKhaiGrade())
        .build();
  }

}
