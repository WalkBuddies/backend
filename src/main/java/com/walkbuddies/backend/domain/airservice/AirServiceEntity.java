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
  @Column(name = "station_code")
  private int stationCode;
  @Column(name = "station_name")
  private String stationName;
  @Column(name = "sido_name")
  private String sidoName;
  @Column(name = "data_time")
  private LocalDateTime dataTime;
  @Column(name = "so2_value")
  private Float so2Value;
  @Column(name = "co_value")
  private Float coValue;
  @Column(name = "o3_value")
  private Float o3Value;
  @Column(name = "no2_value")
  private Float no2Value;
  @Column(name = "pm10_value")
  private int pm10Value;
  @Column(name = "pm25_value")
  private int pm25Value;
  @Column(name = "khai_value")
  private int khaiValue;
  @Column(name = "khai_grade")
  private int khaiGrade;
  @Column(name = "so2_grade")
  private int so2Grade;
  @Column(name = "co_grade")
  private int coGrade;
  @Column(name = "o3_grade")
  private int o3Grade;
  @Column(name = "pm10_grade")
  private int pm10Grade;
  @Column(name = "pm25_grade")
  private int pm25Grade;


  public AirServiceEntity(AirServiceDto airServiceDto) {
    this.stationName = airServiceDto.getStationName();
    this.stationCode = airServiceDto.getStationCode();
    this.dataTime = airServiceDto.getDataTime();
    this.so2Value = airServiceDto.getSo2Value();
    this.coValue = airServiceDto.getCoValue();
    this.o3Value = airServiceDto.getO3Value();
    this.no2Value = airServiceDto.getNo2Value();
    this.pm10Value = airServiceDto.getPm10Value();
    this.pm25Value = airServiceDto.getPm25Value();
    this.khaiValue = airServiceDto.getKhaiValue();
    this.khaiGrade = airServiceDto.getKhaiGrade();
    this.so2Grade = airServiceDto.getSo2Grade();
    this.coGrade = airServiceDto.getCoGrade();
    this.o3Grade = airServiceDto.getO3Grade();
    this.pm10Grade = airServiceDto.getPm10Grade();
    this.pm25Grade = airServiceDto.getPm25Grade();
  }

}
