package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.dto.clubservicec.ClubDto;
import com.walkbuddies.backend.dto.clubservicec.ClubResponse;
import org.springframework.stereotype.Service;

@Service
public interface ClubService {
    /**
     * 소모임을 생성하는 메서드
     * @param clubDto
     * @return
     */
    ClubResponse createClub(ClubDto clubDto);

}
