package com.walkbuddies.backend.controller.clubservice;

import com.walkbuddies.backend.dto.clubservicec.ClubDto;
import com.walkbuddies.backend.dto.clubservicec.ClubResponse;
import com.walkbuddies.backend.service.clubservice.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/club/create")
    public ClubResponse createClub(@RequestBody ClubDto clubDto) {
        return clubService.createClub(clubDto);
    }
}
