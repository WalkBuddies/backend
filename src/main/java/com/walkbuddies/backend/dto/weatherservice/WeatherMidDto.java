package com.walkbuddies.backend.dto.weatherservice;

import com.walkbuddies.backend.domain.weatherservice.WeatherMidEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherMidDto {
    private String regId;
    private String cityName;
    private String tmFc;

    private Integer taMin3;
    private Integer taMax3;
    private Integer taMin4;
    private Integer taMax4;
    private Integer taMin5;
    private Integer taMax5;
    private Integer taMin6;
    private Integer taMax6;
    private Integer taMin7;
    private Integer taMax7;

    private Integer rnSt3Am;
    private Integer rnSt3Pm;
    private Integer rnSt4Am;
    private Integer rnSt4Pm;
    private Integer rnSt5Am;
    private Integer rnSt5Pm;
    private Integer rnSt6Am;
    private Integer rnSt6Pm;
    private Integer rnSt7Am;
    private Integer rnSt7Pm;

    private String wf3Am;
    private String wf3Pm;
    private String wf4Am;
    private String wf4Pm;
    private String wf5Am;
    private String wf5Pm;
    private String wf6Am;
    private String wf6Pm;
    private String wf7Am;
    private String wf7Pm;

    private LocalDate regDate;
    private LocalDate modDate;


    public static WeatherMidResponse of(int statusCode, String message, List<WeatherMidEntity> weatherMidEntities) {

        List<WeatherMidDto> weatherMidDtoList = new ArrayList<>();

        for (WeatherMidEntity weatherMidEntity : weatherMidEntities) {
            WeatherMidDto weatherMidDto = WeatherMidDto.builder()
                    .regId(weatherMidEntity.getRegId())
                    .cityName(weatherMidEntity.getCityName())
                    .tmFc(weatherMidEntity.getTmFc())
                    .taMin3(weatherMidEntity.getTaMin3())
                    .taMax3(weatherMidEntity.getTaMax3())
                    .taMin4(weatherMidEntity.getTaMin4())
                    .taMax4(weatherMidEntity.getTaMax4())
                    .taMin5(weatherMidEntity.getTaMin5())
                    .taMax5(weatherMidEntity.getTaMax5())
                    .taMin6(weatherMidEntity.getTaMin6())
                    .taMax6(weatherMidEntity.getTaMax6())
                    .taMin7(weatherMidEntity.getTaMin7())
                    .taMax7(weatherMidEntity.getTaMax7())
                    .rnSt3Am(weatherMidEntity.getRnSt3Am())
                    .rnSt3Pm(weatherMidEntity.getRnSt3Pm())
                    .rnSt4Am(weatherMidEntity.getRnSt4Am())
                    .rnSt4Pm(weatherMidEntity.getRnSt4Pm())
                    .rnSt5Am(weatherMidEntity.getRnSt5Am())
                    .rnSt5Pm(weatherMidEntity.getRnSt5Pm())
                    .rnSt6Am(weatherMidEntity.getRnSt6Am())
                    .rnSt6Pm(weatherMidEntity.getRnSt6Pm())
                    .rnSt7Am(weatherMidEntity.getRnSt7Am())
                    .rnSt7Pm(weatherMidEntity.getRnSt7Pm())
                    .wf3Am(weatherMidEntity.getWf3Am())
                    .wf3Pm(weatherMidEntity.getWf3Pm())
                    .wf4Am(weatherMidEntity.getWf4Am())
                    .wf4Pm(weatherMidEntity.getWf4Pm())
                    .wf5Am(weatherMidEntity.getWf5Am())
                    .wf5Pm(weatherMidEntity.getWf5Pm())
                    .wf6Am(weatherMidEntity.getWf6Am())
                    .wf6Pm(weatherMidEntity.getWf6Pm())
                    .wf7Am(weatherMidEntity.getWf7Am())
                    .wf7Pm(weatherMidEntity.getWf7Pm())
                    .regDate(weatherMidEntity.getRegDate())
                    .modDate(weatherMidEntity.getModDate())
                    .build();

            weatherMidDtoList.add(weatherMidDto);
        }

        return new WeatherMidResponse(statusCode, message, weatherMidDtoList);
    }
}
