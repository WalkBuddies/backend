package com.walkbuddies.backend.admin.controller;

import com.walkbuddies.backend.admin.service.AdminClubService;
import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.common.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminClubController {

    private final AdminClubService adminClubService;

    @PostMapping("/admin/club/status")
    public ResponseEntity<SingleResponse> setClubStatus(@RequestParam(name = "clubId") Long clubId,
                                                        @RequestParam(name = "suspended") boolean suspended) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "clubId: " + clubId + "에 해당하는 소모임의 상태를 변경했습니다.",
                adminClubService.setClubStatus(clubId, suspended));
        return ResponseEntity.ok(singleResponse);
    }

    @PostMapping("/admin/club/delete")
    public ResponseEntity<SingleResponse> deleteClub(@RequestParam(name = "clubId") Long clubId) {

        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(), "clubId: " + clubId + "에 해당하는 소모임을 삭제했습니다.",
                adminClubService.deleteClub(clubId));
        return ResponseEntity.ok(singleResponse);
    }
}
