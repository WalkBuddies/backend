package com.walkbuddies.backend.weather.domain;

import com.walkbuddies.backend.club.domain.TownEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_short_term")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherShortEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shortTermId;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private TownEntity town;

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
