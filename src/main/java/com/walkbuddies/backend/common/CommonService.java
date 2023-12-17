package com.walkbuddies.backend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public interface CommonService {
    /**
     * 경위도 tm좌표 변환
     *
     * @param x 경도
     * @param y 위도
     * @return
     * @throws URISyntaxException
     * @throws JsonProcessingException
     */
    double[] GeoToTm(double x, double y) throws URISyntaxException, JsonProcessingException;

}
