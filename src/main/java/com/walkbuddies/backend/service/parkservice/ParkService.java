package com.walkbuddies.backend.service.parkservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.dto.parkservice.ParkDto;
import org.springframework.stereotype.Service;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public interface ParkService {

    /**
     * 공원정보 open api 요청 url 생성 메서드
     * @param pageNo
     * @param numOfRows
     * @return
     */
    String buildParkAPIUrl(int pageNo, int numOfRows);

    /**
     * open api에서 데이터를 가져오는 메서드
     * @param apiUrl
     * @return
     * @throws URISyntaxException
     */
    String fetchDataFromApi(String apiUrl) throws URISyntaxException;

    /**
     * 가져올 데이터가 더 존재하는지 확인하는 메서드
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    boolean hasMoreData(String response) throws JsonProcessingException;

    /**
     * 가져온 데이터 파싱하여 ParkDto 리스트로 변환하는 메서드
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    List<ParkDto> parseApiResponse(String response) throws JsonProcessingException;

    /**
     * 주소가 이미 존재하는 경우 업데이트, 없다면 저장하는 메서드
     * @param parkDtoList
     */
    void saveAllParks(List<ParkDto> parkDtoList);

    /**
     * 위도, 경도를 받아 반경 1km 내의 공원 목록을 반환하는 메서드
     * @param longitude
     * @param latitude
     * @return
     */
    List<ParkDto> getParkList(float longitude, float latitude);

    /**
     * parkId로 공원 정보를 반환하는 메서드
     * @param parkId
     * @return
     */
    Optional<ParkDto> getParkInfo(int parkId);

    /**
     * 공원 정보 추가하는 메서드
     * @param parkDto
     */
    void addPark(ParkDto parkDto);

    /**
     * 공원 정보 수정하는 메서드
     * @param parkId
     * @param newDto
     */
    void updatePark(int parkId, ParkDto newDto);

    /**
     * 공원 정보 삭제하는 메서드
     * @param parkId
     */
    void deletePark(int parkId);
}
