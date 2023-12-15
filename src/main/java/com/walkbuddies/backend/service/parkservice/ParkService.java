package com.walkbuddies.backend.service.parkservice;

import com.walkbuddies.backend.dto.parkservice.ParkDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParkService {

    /**
     * 공원 정보 업데이트
     */
    void updateParkData();

    /**
     * 공원정보 open api 요청 url 생성
     * @param pageNo
     * @param numOfRows
     * @return
     */
    String buildParkAPIUrl(int pageNo, int numOfRows);

    /**
     * open api에서 데이터를 가져오는 메서드
     * @param apiUrl
     * @return
     */
    String fetchDataFromApi(String apiUrl);

    /**
     * 가져올 데이터가 더 존재하는지 확인
     * @param response
     * @return
     */
    boolean hasMoreData(String response);

    /**
     * 가져온 데이터 파싱하여 ParkDto 리스트로 변환
     * @param response
     * @return
     */
    List<ParkDto> parseApiResponse(String response);

    /**
     * 주소가 이미 존재하는 경우 업데이트, 없다면 저장
     * @param parkDtoList
     */
    void saveAllParks(List<ParkDto> parkDtoList);

    /**
     * 위도, 경도를 받아 반경 1km 내의 공원 목록을 반환
     * @param longitude
     * @param latitude
     * @return
     */
    List<ParkDto> getParkList(Double longitude, Double latitude);

    /**
     * parkId로 공원 정보를 반환
     *
     * @param parkId
     * @return
     */
    ParkDto getParkInfo(Long parkId);

    /**
     * 공원 정보 추가
     * @param parkDto
     */
    void addPark(ParkDto parkDto);

    /**
     * 공원 정보 수정
     * @param parkId
     * @param newDto
     */
    void updatePark(Long parkId, ParkDto newDto);

    /**
     * 공원 정보 삭제
     * @param parkId
     */
    void deletePark(Long parkId);

    /**
     * 멤버가 즐겨찾기한 공원 목록 조회
     * @param memberId
     * @return
     */
    List<ParkDto> getFavoritePark(Long memberId);

    /**
     * 공원 즐겨찾기 추가
     * @param memberId
     * @param parkId
     */
    void addFavoritePark(Long memberId, Long parkId);

    /**
     * 공원 즐겨찾기 삭제
     * @param memberId
     * @param parkId
     */
    void deleteFavoritePark(Long memberId, Long parkId);
}
