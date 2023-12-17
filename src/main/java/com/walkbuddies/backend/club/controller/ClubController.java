package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.dto.ClubResponse;
import com.walkbuddies.backend.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/club/create")
    public ResponseEntity<ClubResponse> createClub(@RequestBody ClubDto clubDto) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(),
                clubDto.getClubName() + " 소모임을 생성 완료 했습니다.",
                clubService.createClub(clubDto));

        return ResponseEntity.ok(clubResponse);
    }

    @GetMapping("/club/search")
    public ResponseEntity<ClubResponse> searchClub(@RequestParam(name = "townId") Long townId, @RequestParam(name = "clubName") String clubName) {

        List<String> foundClubs = clubService.searchClub(townId, clubName);
        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(),
                clubName + "이(가) 포함된 소모임이 검색되었습니다.",
                ClubDto.builder().clubName(foundClubs.toString()).build());

        return ResponseEntity.ok(clubResponse);
    }

    @PostMapping("/club/join/request")
    public ResponseEntity<ClubResponse> joinClubRequest(@RequestBody ClubJoinInform clubJoinInform) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(), clubService.joinClubRequest(clubJoinInform), null);
        return ResponseEntity.ok(clubResponse);
    }

    @GetMapping("/club/waiting/get")
    public ResponseEntity<ClubResponse> getClubWaitingData(@RequestParam(name = "clubId") Long clubId) {

        List<String> foundMembers = clubService.getClubWaitingData(clubId);
        ClubDto clubDto = ClubDto.builder()
                .clubId(clubId)
                .waitingMembers(foundMembers)
                .build();
        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(),
                "소모임 ID: " + clubId + "에 가입 신청한 회원 목록입니다.", clubDto);

        return ResponseEntity.ok(clubResponse);
    }

    @PostMapping("/club/join/response")
    public ResponseEntity<ClubResponse> joinClubResponse(@RequestParam(name = "allowJoin") boolean allowJoin,
                                   @RequestBody ClubJoinInform clubJoinInform) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(), clubService.joinClubResponse(allowJoin, clubJoinInform), null);
        return ResponseEntity.ok(clubResponse);
    }
}