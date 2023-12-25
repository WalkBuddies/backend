package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.dto.ClubListResponse;
import com.walkbuddies.backend.club.dto.ClubResponse;
import com.walkbuddies.backend.club.service.ClubService;
import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
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
    public ResponseEntity<SingleResponse> createClub(@RequestBody ClubDto clubDto) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                clubDto.getClubName() + " 소모임을 생성 완료했습니다.",
                clubService.createClub(clubDto));

        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("/club/delete")
    public ResponseEntity<SingleResponse> deleteClub(@RequestParam(name = "ownerId") Long ownerId,
                                                   @RequestParam(name = "clubId") Long clubId) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 폐쇄를 완료했습니다.",
                clubService.deleteClub(ownerId, clubId));

        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("/club/delete")
    public ResponseEntity<ClubResponse> deleteClub(@RequestParam(name = "ownerId") Long ownerId,
                                                   @RequestParam(name = "clubId") Long clubId) {

        ClubResponse clubResponse = new ClubResponse(HttpStatus.OK.value(), "소모임 폐쇄를 완료했습니다.",
                clubService.deleteClub(ownerId, clubId));

        return ResponseEntity.ok(clubResponse);
    }

    @GetMapping("/club/search")

    public ResponseEntity<ListResponse> searchClub(@RequestParam(name = "townId") Long townId,
                                                   @RequestParam(name = "clubName") String clubName) {

        List<String> foundClubs = clubService.searchClub(townId, clubName);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                clubName + "이(가) 포함된 소모임이 검색되었습니다.", foundClubs);

        return ResponseEntity.ok(listResponse);
    }

    @PostMapping("/club/join/request")
    public ResponseEntity<SingleResponse> joinClubRequest(@RequestBody ClubJoinInform clubJoinInform) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                "소모임 가입 신청이 완료 되었습니다.",
                clubService.joinClubRequest(clubJoinInform));
        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/club/join/waiting")

    public ResponseEntity<ListResponse> getClubWaitingData(@RequestParam(name = "clubId") Long clubId) {
      
        List<String> foundMembers = clubService.getClubWaitingData(clubId);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                "소모임 ID: " + clubId + "에 가입 신청한 회원 목록입니다.", foundMembers);

        return ResponseEntity.ok(listResponse);
    }

    @PostMapping("/club/join/response")
    public ResponseEntity<SingleResponse> joinClubResponse(@RequestParam(name = "allowJoin") boolean allowJoin,
                                                         @RequestBody ClubJoinInform clubJoinInform) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 가입 신청에 대한 응답입니다.",
                clubService.joinClubResponse(allowJoin, clubJoinInform));
        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("club/leave")
    public ResponseEntity<SingleResponse> leaveClub(@RequestParam(name = "clubId") Long clubId,
                                                  @RequestParam(name = "memberId") Long memberId) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 탈퇴가 완료 되었습니다.",
                clubService.leaveClub(clubId, memberId));
        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("/club/update")
    public ResponseEntity<SingleResponse> updateClubData(@RequestParam(name = "ownerId") Long ownerId,
                                                       @RequestBody  ClubDto clubDto) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 정보를 수정했습니다.", clubService.updateClubData(ownerId, clubDto));
        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/club/myclub")
    public ResponseEntity<ClubListResponse> getMyClub(@RequestParam(name = "memberId") Long memberId) {

       ClubListResponse clubListResponse = new ClubListResponse(HttpStatus.OK.value(),
               "가입한 소모임 목록을 불러왔습니다.", clubService.getMyClub(memberId));

       return ResponseEntity.ok(listResponse);

}
