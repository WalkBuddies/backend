package com.walkbuddies.backend.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.weather.service.WeatherMidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherMidController {

    private final WeatherMidService weatherMidService;

    @PostMapping("/weather/mid/update")
    public SingleResponse weatherMidUpdate(@RequestParam(name = "tmFc") String tmFc) throws JsonProcessingException {

        weatherMidService.update(tmFc);
        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "중기예보 정보 업데이트에 성공하였습니다.", null);
        return singleResponse;
    }

    @GetMapping("weather/mid/data")
    public ListResponse getWeatherMidData(@RequestParam(name = "cityName") String cityName) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(), "중기예보 정보를 조회하는데 성공하였습니다.",
                weatherMidService.getData(cityName));
        return listResponse;
    }
}
