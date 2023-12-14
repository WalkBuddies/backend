package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.dto.clubservice.ClubDto;
import com.walkbuddies.backend.dto.clubservice.ClubJoinInform;
import com.walkbuddies.backend.dto.clubservice.ClubResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClubService {
    /**
     * 소모임을 생성하는 메서드
     * @param clubDto
     * @return
     */
    ClubResponse createClub(ClubDto clubDto);

    /**
     * 소모임 검색 기능 메서드.
     * @param clubName
     * @return
     */
    List<String> searchClub(Long townId, String clubName);

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     * @param clubJoinInform
     * @return
     */
    String joinClubRequest(ClubJoinInform clubJoinInform);
}
