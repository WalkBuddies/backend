package com.walkbuddies.backend.domain.weatherservice;

import jakarta.persistence.*;
import lombok.*;

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
}
