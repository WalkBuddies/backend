package com.walkbuddies.backend.admin.service;

import com.walkbuddies.backend.club.dto.ClubDto;
import org.springframework.stereotype.Service;

@Service
public interface AdminClubService {

    /**
     * 소모임 상태 설정 메서드
     * @param clubId
     * @param suspended
     * @return
     */
    ClubDto setClubStatus(Long clubId, boolean suspended);

    /**
     * 소모임을 삭제하는 메서드
     * @param clubId
     * @return
     */
    ClubDto deleteClub(Long clubId);
}
