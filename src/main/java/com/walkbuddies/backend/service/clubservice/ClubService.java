package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.dto.clubservice.ClubDto;
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
    List<String> searchClub(String clubName);
}
