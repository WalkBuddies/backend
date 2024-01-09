package com.walkbuddies.backend.club.controller;

import com.walkbuddies.backend.club.dto.form.ClubCreateParameter;
import com.walkbuddies.backend.club.dto.form.ClubJoinParameter;
import com.walkbuddies.backend.club.dto.form.ClubParameter;
import com.walkbuddies.backend.club.dto.ClubPrefaceDto;
import com.walkbuddies.backend.club.dto.form.ClubUpdateParameter;
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
    public SingleResponse createClub(@RequestBody ClubCreateParameter clubCreateParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                clubCreateParameter.getClubName() + " 소모임을 생성 완료했습니다.",
                clubService.create(clubCreateParameter));

        return singleResponse;
    }

    @PostMapping("/club/delete")
    public SingleResponse deleteClub(@RequestBody ClubParameter clubParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "소모임 폐쇄를 완료했습니다.",
                clubService.delete(clubParameter));

        return singleResponse;
    }

    @GetMapping("/club/search")
    public ListResponse searchClub(@RequestBody ClubParameter clubParameter) {

        List<String> foundClubs = clubService.search(clubParameter);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                clubParameter.getClubName() + "이(가) 포함된 소모임이 검색되었습니다.", foundClubs);

        return listResponse;
    }

    @PostMapping("/club/join/request")
    public SingleResponse joinClubRequest(@RequestBody ClubJoinParameter clubJoinParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                "소모임 가입 신청이 완료 되었습니다.",
                clubService.joinRequest(clubJoinParameter));
        return singleResponse;
    }

    @GetMapping("/club/join/waiting")
    public ListResponse getClubWaitingData(@RequestBody ClubParameter clubParameter) {

        List<String> foundMembers = clubService.getWaitingData(clubParameter);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                "가입 신청한 회원 목록입니다.", foundMembers);

        return listResponse;
    }

    @PostMapping("/club/join/response")
    public SingleResponse joinClubResponse(@RequestBody ClubJoinParameter clubJoinParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                "소모임 가입 신청에 대한 응답입니다.",
                clubService.joinResponse(clubJoinParameter));
        return singleResponse;
    }

    @PostMapping("club/leave")
    public SingleResponse leaveClub(@RequestBody ClubParameter clubParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                "소모임 탈퇴가 완료 되었습니다.",
                clubService.leave(clubParameter));
        return singleResponse;
    }

    @PostMapping("/club/update")
    public SingleResponse updateClubData(@RequestBody ClubUpdateParameter clubUpdateParameter) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                "소모임 정보를 수정했습니다.",
                clubService.update(clubUpdateParameter));
        return singleResponse;
    }

    @GetMapping("/club/myClub")
    public ListResponse getMyClub(@RequestBody ClubParameter clubParameter) {

        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
               "가입한 소모임 목록을 불러왔습니다.", clubService.getMyClub(clubParameter));
       return listResponse;
    }

    @PostMapping("/club/preface/{clubId}/new")
    public ResponseEntity<SingleResponse<ClubPrefaceDto>> createPreface(@PathVariable Long clubId, @RequestBody ClubPrefaceDto dto) {
        dto.setClubId(clubId);
        SingleResponse<ClubPrefaceDto> response = new SingleResponse<>(HttpStatus.CREATED.value(), "말머리가 생성 완료 되었습니다.",
            clubService.createPreface(dto));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/club/preface/{clubId}/update")
    public ResponseEntity<SingleResponse<ClubPrefaceDto>> updatePreface(@PathVariable Long clubId, @RequestBody ClubPrefaceDto dto) {
        dto.setClubId(clubId);
        SingleResponse<ClubPrefaceDto> response = new SingleResponse<>(HttpStatus.OK.value(), "말머리가 수정 완료 되었습니다",
            clubService.updatePreface(dto));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/club/preface/{prefaceId}")
    public ResponseEntity<SingleResponse<ClubPrefaceDto>> deletePreface(@PathVariable Long prefaceId) {
        clubService.deletePreface(prefaceId);
        SingleResponse<ClubPrefaceDto> response = new SingleResponse<>(HttpStatus.OK.value(), "말머리가 삭제 되었습니다", null);

        return ResponseEntity.ok(response);

    }
}
