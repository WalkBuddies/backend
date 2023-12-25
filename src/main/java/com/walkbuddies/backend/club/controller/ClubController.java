package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.dto.ClubParameter;
import com.walkbuddies.backend.club.dto.ClubUpdateParameter;
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
    public ResponseEntity<SingleResponse> deleteClub(@RequestBody ClubParameter clubParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 폐쇄를 완료했습니다.",
                clubService.deleteClub(clubParameter));

        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/club/search")
    public ResponseEntity<ListResponse> searchClub(@RequestBody ClubParameter clubParameter) {

        List<String> foundClubs = clubService.searchClub(clubParameter);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                clubParameter.getClubName() + "이(가) 포함된 소모임이 검색되었습니다.", foundClubs);

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
    public ResponseEntity<ListResponse> getClubWaitingData(@RequestBody ClubParameter clubParameter) {

        List<String> foundMembers = clubService.getClubWaitingData(clubParameter);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                "소모임 ID: " + clubParameter.getClubId() + "에 가입 신청한 회원 목록입니다.", foundMembers);

        return ResponseEntity.ok(listResponse);
    }

    @PostMapping("/club/join/response")
    public ResponseEntity<SingleResponse> joinClubResponse(@RequestBody ClubJoinInform clubJoinInform) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 가입 신청에 대한 응답입니다.",
                clubService.joinClubResponse(clubJoinInform));
        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("club/leave")
    public ResponseEntity<SingleResponse> leaveClub(@RequestBody ClubParameter clubParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 탈퇴가 완료 되었습니다.",
                clubService.leaveClub(clubParameter));
        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("/club/update")
    public ResponseEntity<SingleResponse> updateClubData(@RequestBody ClubUpdateParameter clubUpdateParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 정보를 수정했습니다.", clubService.updateClubData(clubUpdateParameter));
        return ResponseEntity.ok(singleResponse);
    }

    @GetMapping("/club/myclub")
    public ResponseEntity<ListResponse> getMyClub(@RequestBody ClubParameter clubParameter) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
               "가입한 소모임 목록을 불러왔습니다.", clubService.getMyClub(clubParameter));
       return ResponseEntity.ok(listResponse);
    }
}
