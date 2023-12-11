package com.walkbuddies.backend.controller.weatherservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.weatherservicec.dto.weathermid.WeatherMidDto;
import com.walkbuddies.weatherservicec.dto.weathermid.WeatherMidResponse;
import com.walkbuddies.weatherservicec.service.weathermid.WeatherMidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class WeatherMidController {

    private final WeatherMidService weatherMidService;

    @PostMapping("/weather/mid/update")
    public String weatherMidUpdate(@RequestParam String tmFc) throws JsonProcessingException {
        return weatherMidService.updateWeatherMidData(tmFc);
    }

    @GetMapping("weather/mid/data")
    public WeatherMidResponse getWeatherMidData(@RequestParam String cityName) {
        return weatherMidService.getWeatherMidData(cityName);
    }
}
