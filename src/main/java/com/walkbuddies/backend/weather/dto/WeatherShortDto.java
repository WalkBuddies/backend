package com.walkbuddies.backend.weather.dto;

import com.walkbuddies.backend.club.domain.TownEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherShortDto {

    private Integer nx;
    private Integer ny;
    private Double pop;
    private Integer pty;
    private String pcp;
    private String sno;
    private Integer sky;
    private Integer tmp;
    private Integer tmn;
    private Integer tmx;
    private Double wsd;
    private String baseDate;
    private String baseTime;
    private String fcstDate;
    private String fcstTime;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
