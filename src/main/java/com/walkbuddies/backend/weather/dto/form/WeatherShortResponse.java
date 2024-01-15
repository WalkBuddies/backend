package com.walkbuddies.backend.weather.dto.form;

import com.walkbuddies.backend.weather.domain.WeatherShortEntity;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherShortResponse {

    private String townName;
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

    public static WeatherShortResponse of(WeatherShortEntity weatherShortEntity) {

        return WeatherShortResponse.builder()
                .townName(weatherShortEntity.getTown().getTownName())
                .pop(weatherShortEntity.getPop())
                .pty(weatherShortEntity.getPty())
                .pcp(weatherShortEntity.getPcp())
                .sno(weatherShortEntity.getSno())
                .sky(weatherShortEntity.getSky())
                .tmp(weatherShortEntity.getTmp())
                .tmn(weatherShortEntity.getTmn())
                .tmx(weatherShortEntity.getTmx())
                .wsd(weatherShortEntity.getWsd())
                .baseDate(weatherShortEntity.getBaseDate())
                .baseTime(weatherShortEntity.getBaseTime())
                .fcstDate(weatherShortEntity.getFcstDate())
                .fcstTime(weatherShortEntity.getFcstTime())
                .build();
    }
}
