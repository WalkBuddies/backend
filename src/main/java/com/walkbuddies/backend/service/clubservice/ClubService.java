package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.dto.clubservicec.ClubDto;
import com.walkbuddies.backend.dto.clubservicec.ClubResponse;
import org.springframework.stereotype.Service;

@Service
public interface ClubService {
    ClubResponse createClub(ClubDto clubDto);
}
