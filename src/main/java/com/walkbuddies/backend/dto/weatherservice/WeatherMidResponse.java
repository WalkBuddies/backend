package com.walkbuddies.weatherservicec.dto.weathermid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class WeatherMidResponse {
    private final int statusCode;
    private final String message;
    private final List<WeatherMidDto> data;
}
