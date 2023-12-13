package com.walkbuddies.backend.controller.clubservice;

import com.walkbuddies.backend.dto.clubservice.ClubDto;
import com.walkbuddies.backend.dto.clubservice.ClubResponse;
import com.walkbuddies.backend.service.clubservice.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/club/create")
    public ClubResponse createClub(@RequestBody ClubDto clubDto) {
        return clubService.createClub(clubDto);
    }

    @GetMapping("/club/search")
    public List<String> searchClub(@RequestParam String clubName) {
        return clubService.searchClub(clubName);
    }
}
