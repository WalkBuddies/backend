package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.form.*;
import com.walkbuddies.backend.club.dto.ClubPrefaceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClubService {
    /**
     * 소모임을 생성하는 메서드
     *
     * @param clubCreateParameter
     * @return
     */
    ClubResponse create(ClubCreateParameter clubCreateParameter);

    /**
     * 소모임을 폐쇄하는 메서드
     * @param clubId
     * @param ownerId
     * @return
     */
    ClubResponse delete(Long clubId, Long ownerId);

    /**
     * 소모임 검색 기능 메서드.
     * 검색어가 포함되고 검색이 되도록 한 소모임에 한에서 모두 검색이 됨.
     *
     * @param clubName
     * @param townId
     * @return
     */
    List<String> search(String clubName, Long townId);

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     *
     * @param clubJoinParameter
     * @return
     */
    String joinRequest(ClubJoinParameter clubJoinParameter);

    /**
     * 소모임 Id를 기반으로 소모임의 가입 신청자를 볼 수 있는 메서드.
     * 신청자의 memberId와 가입시 작성했던 message를 함께 볼 수 있음.
     *
     * @param clubId
     * @return
     */
    List<String> getWaitingData(Long clubId);

    /**
     * 소모임 신청자 가입 승인, 거절 메서드.
     * 승인에 대한 Boolean 타입과 정보를 받아서 승인할지 거절할지 정함.
     *
     * @param clubJoinParameter
     * @return
     */
    String joinResponse(ClubJoinParameter clubJoinParameter);

    /**
     * 소모임 탈퇴 메서드.
     *
     * @param clubId
     * @param memberId
     * @return
     */
    String leave(Long clubId, Long memberId);

    /**
     * 소모임 정보를 수정하는 메서드
     *
     * @param clubUpdateParameter
     * @return
     */
    ClubResponse update(ClubUpdateParameter clubUpdateParameter);

    /**
     * 내 소모임 목록 보기 메서드
     * @param memberId
     * @return
     */
    List<ClubResponse> getMyClub(Long memberId);

    /**
     * 말머리 만들기
     * @param clubPrefaceDto
     * @return
     */
    ClubPrefaceDto createPreface(ClubPrefaceDto clubPrefaceDto);

    /**
     * 말머리 수정
     * @param clubPrefaceDto
     * @return
     */
    ClubPrefaceDto updatePreface(ClubPrefaceDto clubPrefaceDto);

    /**
     * 말머리 삭제
     * @param prefaceId
     */
    void deletePreface(Long prefaceId);
}
