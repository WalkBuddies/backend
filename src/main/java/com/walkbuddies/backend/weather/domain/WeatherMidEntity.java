package com.walkbuddies.backend.weather.domain;

import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "weather_mid_term")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherMidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long midTermId;
    private String regId;
    private String cityName;
    private String tmFc;

    @Column(name = "3t_min")
    private Integer taMin3;
    @Column(name = "3t_max")
    private Integer taMax3;
    @Column(name = "4t_min")
    private Integer taMin4;
    @Column(name = "4t_max")
    private Integer taMax4;
    @Column(name = "5t_min")
    private Integer taMin5;
    @Column(name = "5t_max")
    private Integer taMax5;
    @Column(name = "6t_min")
    private Integer taMin6;
    @Column(name = "6t_max")
    private Integer taMax6;
    @Column(name = "7t_min")
    private Integer taMin7;
    @Column(name = "7t_max")
    private Integer taMax7;

    @Column(name = "3r_am")
    private Integer rnSt3Am;
    @Column(name = "3r_pm")
    private Integer rnSt3Pm;
    @Column(name = "4r_am")
    private Integer rnSt4Am;
    @Column(name = "4r_pm")
    private Integer rnSt4Pm;
    @Column(name = "5r_am")
    private Integer rnSt5Am;
    @Column(name = "5r_pm")
    private Integer rnSt5Pm;
    @Column(name = "6r_am")
    private Integer rnSt6Am;
    @Column(name = "6r_pm")
    private Integer rnSt6Pm;
    @Column(name = "7r_am")
    private Integer rnSt7Am;
    @Column(name = "7r_pm")
    private Integer rnSt7Pm;

    @Column(name = "3w_am")
    private String wf3Am;
    @Column(name = "3w_pm")
    private String wf3Pm;
    @Column(name = "4w_am")
    private String wf4Am;
    @Column(name = "4w_pm")
    private String wf4Pm;
    @Column(name = "5w_am")
    private String wf5Am;
    @Column(name = "5w_pm")
    private String wf5Pm;
    @Column(name = "6w_am")
    private String wf6Am;
    @Column(name = "6w_pm")
    private String wf6Pm;
    @Column(name = "7w_am")
    private String wf7Am;
    @Column(name = "7w_pm")
    private String wf7Pm;

    private LocalDate regDate;
    private LocalDate modDate;

    public static WeatherMidDto dtoToEntity(WeatherMidEntity weatherMidEntity) {
        return WeatherMidDto.builder()
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
    }
}
