package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.dto.ClubResponse;
import com.walkbuddies.backend.club.service.ClubService;
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
    public List<String> searchClub(@RequestParam Long townId, @RequestParam String clubName) {
        return clubService.searchClub(townId, clubName);
    }

    @PostMapping("/club/join/request")
    public String joinClubRequest(@RequestBody ClubJoinInform clubJoinInform) {

        return clubService.joinClubRequest(clubJoinInform);
    }

    @GetMapping("/club/waiting/get")
    public List<String> getClubWaitingData(@RequestParam Long clubId) {
        return clubService.getClubWaitingData(clubId);
    }

    @PostMapping("/club/join/response")
    public String joinClubResponse(@RequestParam boolean allowJoin,
                                   @RequestBody ClubJoinInform clubJoinInform) {
        return clubService.joinClubResponse(allowJoin, clubJoinInform);
    }
}
