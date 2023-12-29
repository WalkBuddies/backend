package com.walkbuddies.backend.air.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.air.domain.AirServiceEntity;
import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.air.dto.MsrstnDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public interface AirService {

    /**
     * 요청좌표의 미세먼지정보를 찾는 메소드
     * 최근접측정소를 찾은 후 측정소 코드를 통해 db에 저장된 정보가 없다면 미세먼지 api에서 정보를 받음
     * db에 저장된 정보가 조회시간보다 1시간 이내면 db의 정보를 반환, 아니면 api에서 정보를 받음
     *
     * @param x 경도
     * @param y 위도
     * @return
     * @throws
     */
    AirServiceDto getAirInfo(double x, double y) throws URISyntaxException, JsonProcessingException;

    /**
     * 요청받은 좌표로부터 최근접측정소를 찾는 api 호출
     *
     * @param tmX 입력받은 tmx 좌표
     * @param tmY 입력받은 tmy 좌표
     * @return 최근접측정소 정보(msrstnDto) 리턴
     * @throws IOException
     */
    MsrstnDto getNearbyMsrstnInfoFromApi(double tmX, double tmY)
            throws IOException, URISyntaxException;

    /**
     * 미세먼지 api 조회
     *
     * @param msrstnDto 측정소정보 dto
     * @return
     * @throws IOException
     */
    AirServiceEntity getAirInfoFromApi(MsrstnDto msrstnDto) throws IOException, URISyntaxException;

}
