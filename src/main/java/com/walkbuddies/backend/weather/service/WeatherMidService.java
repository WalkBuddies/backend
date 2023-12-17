package com.walkbuddies.backend.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.weather.domain.WeatherMidEntity;
import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import com.walkbuddies.backend.weather.dto.WeatherMidResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherMidService {

    /**
     * 주간 최저, 최고 기온을 Oepn API를 통해 데이터를 가져오는 메서드
     *
     * @param regId
     * @param tmFc
     * @return
     * @throws JsonProcessingException
     */
    Object getWeatherMidTa(String regId, String tmFc) throws JsonProcessingException;

    /**
     * 주간 오전, 오후 강수 확률과 날씨예보를 Open API를 통해 데이터를 가져오는 메서드
     *
     * @param regId
     * @param tmFc
     * @return
     * @throws JsonProcessingException
     */
    Object getWeatherMidLandFcst(String regId, String tmFc) throws JsonProcessingException;

    /**
     * 지역코드를 기반으로 DB에 이미 저장되어있는 지역이라면 업데이트하고 없는 지역이라면 새로 추가하는 메서드
     *
     * @param tmFc
     * @return
     * @throws JsonProcessingException
     */
    String updateWeatherMidData(String tmFc) throws JsonProcessingException;


    /**
     * DB에서 도시이름에 해당하는 중기예보 데이터를 가져오는 메서드
     *
     * @param cityName
     * @return
     */
    List<WeatherMidDto> getWeatherMidData(String cityName);
}
