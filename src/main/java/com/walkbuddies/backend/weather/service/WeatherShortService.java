package com.walkbuddies.backend.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public interface WeatherShortService {

    /**
     * OpenApi를 통해서 단기예보 데이터를 받아오는 메서드
     *
     * @param x
     * @param y
     * @return
     * @throws JsonProcessingException
     */
    Object getWeatherShortData(Double x, Double y) throws URISyntaxException, JsonProcessingException;

    /**
     * 위경도를 입력하면 그 위경도에 해당하는 지역의 단기예보를 저장하는 메서드
     *
     * @param x
     * @param y
     * @return
     * @throws JsonProcessingException
     */
    String update(Double x, Double y) throws JsonProcessingException;
}
