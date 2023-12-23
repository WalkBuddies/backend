package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.dto.ClubListResponse;
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
                clubDto.getClubName() + " 소모임을 생성 완료했습니다.",
                clubService.createClub(clubDto));

        return ResponseEntity.ok(clubResponse);
    }

    @PostMapping("/club/delete")
    public ResponseEntity<ClubResponse> deleteClub(@RequestParam(name = "ownerId") Long ownerId,
                                                   @RequestParam(name = "clubId") Long clubId) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(), "소모임 폐쇄를 완료했습니다.",
                clubService.deleteClub(ownerId, clubId));

        return ResponseEntity.ok(clubResponse);
    }

    @GetMapping("/club/search")
    public ResponseEntity<ClubResponse> searchClub(@RequestParam(name = "townId") Long townId,
                                                   @RequestParam(name = "clubName") String clubName) {

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

    @GetMapping("/club/join/waiting")
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

    @PostMapping("club/leave")
    public ResponseEntity<ClubResponse> leaveClub(@RequestParam(name = "clubId") Long clubId,
                                                  @RequestParam(name = "memberId") Long memberId) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(), clubService.leaveClub(clubId, memberId), null);
        return ResponseEntity.ok(clubResponse);
    }

    @PostMapping("/club/update")
    public ResponseEntity<ClubResponse> updateClubData(@RequestParam(name = "ownerId") Long ownerId,
                                                       @RequestBody  ClubDto clubDto) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(), "소모임 정보를 수정했습니다.", clubService.updateClubData(ownerId, clubDto));
        return ResponseEntity.ok(clubResponse);
    }

    @GetMapping("/club/myclub")
    public ResponseEntity<ClubListResponse> getMyClub(@RequestParam(name = "memberId") Long memberId) {

       ClubListResponse clubListResponse = new ClubListResponse(HttpStatus.OK.value(),
               "가입한 소모임 목록을 불러왔습니다.", clubService.getMyClub(memberId));
       return ResponseEntity.ok(clubListResponse);
    }
}
