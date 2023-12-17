package com.walkbuddies.backend.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import com.walkbuddies.backend.weather.dto.WeatherMidResponse;
import com.walkbuddies.backend.weather.service.WeatherMidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class WeatherMidController {

    private final WeatherMidService weatherMidService;

    @PostMapping("/weather/mid/update")
    public ResponseEntity<WeatherMidResponse> weatherMidUpdate(@RequestParam(name = "tmFc") String tmFc) throws JsonProcessingException {

        WeatherMidResponse weatherMidResponse = new WeatherMidResponse(HttpStatus.OK.value(), weatherMidService.updateWeatherMidData(tmFc), null);
        return ResponseEntity.ok(weatherMidResponse);
    }

    @GetMapping("weather/mid/data")
    public ResponseEntity<WeatherMidResponse> getWeatherMidData(@RequestParam(name = "cityName") String cityName) {

        WeatherMidResponse weatherMidResponse = new WeatherMidResponse(HttpStatus.OK.value(), "중기예보 정보를 조회하는데 성공하였습니다.", weatherMidService.getWeatherMidData(cityName));
        return ResponseEntity.ok(weatherMidResponse);
    }
}
