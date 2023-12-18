package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.dto.ClubResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClubService {
    /**
     * 소모임을 생성하는 메서드
     *
     * @param clubDto
     * @return
     */
    ClubDto createClub(ClubDto clubDto);

    /**
     * 소모임 검색 기능 메서드.
     * 검색어가 포함되고 검색이 되도록 한 소모임에 한에서 모두 검색이 됨.
     *
     * @param clubName
     * @return
     */
    List<String> searchClub(Long townId, String clubName);

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     *
     * @param clubJoinInform
     * @return
     */
    String joinClubRequest(ClubJoinInform clubJoinInform);

    /**
     * 소모임 Id를 기반으로 소모임의 가입 신청자를 볼 수 있는 메서드.
     * 신청자의 memberId와 가입시 작성했던 message를 함께 볼 수 있음.
     *
     * @param clubId
     * @return
     */
    List<String> getClubWaitingData(Long clubId);

    /**
     * 소모임 신청자 가입 승인, 거절 메서드.
     * 승인에 대한 Boolean 타입과 정보를 받아서 승인할지 거절할지 정함.
     *
     * @param allowJoin
     * @param clubJoinInform
     * @return
     */
    String joinClubResponse(Boolean allowJoin, ClubJoinInform clubJoinInform);
}
