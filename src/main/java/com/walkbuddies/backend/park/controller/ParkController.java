package com.walkbuddies.backend.park.controller;

import com.walkbuddies.backend.common.response.ListResponse;
import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.member.service.MemberService;
import com.walkbuddies.backend.park.dto.ParkDistanceResponse;
import com.walkbuddies.backend.park.dto.ParkRequest;
import com.walkbuddies.backend.park.dto.ParkResponse;
import com.walkbuddies.backend.park.service.ParkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/park")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;
    private final MemberService memberService;

    @GetMapping("/update")
    public ResponseEntity<SingleResponse> updateParkData() {
        parkService.updateParkData();
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원 목록이 업데이트되었습니다.",
                null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ListResponse> getParkList(@RequestParam(name = "longitude") Double longitude, @RequestParam(name = "latitude") Double latitude) {
        List<ParkDistanceResponse> parkDistanceResponses = parkService.getParkList(longitude, latitude);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                "경도: " + longitude + ", 위도: " + latitude + " 지점 1km 이내의 공원입니다.",
                parkDistanceResponses);
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{parkId}")
    public ResponseEntity<SingleResponse> getParkInfo(@PathVariable Long parkId) {
        ParkResponse parkResponse = parkService.getParkInfo(parkId);
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원 정보를 조회했습니다.",
                parkResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SingleResponse> addPark(@RequestBody ParkRequest parkRequest) {
        ParkResponse parkResponse = parkService.addPark(parkRequest);
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원 정보를 등록했습니다.",
                parkResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{parkId}")
    public ResponseEntity<SingleResponse> updatePark(@PathVariable Long parkId, @RequestBody ParkRequest newDto) {
        ParkResponse parkResponse = parkService.updatePark(parkId, newDto);
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원 정보를 수정했습니다.",
                parkResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{parkId}")
    public ResponseEntity<SingleResponse> deletePark(@PathVariable Long parkId) {
        ParkResponse parkResponse = parkService.deletePark(parkId);
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원 정보를 삭제했습니다.",
                parkResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorites/{memberId}")
    public ResponseEntity<ListResponse> getFavoriteParks(@PathVariable Long memberId) {
        List<ParkResponse> favoriteParkList = parkService.getFavoritePark(memberId);
        ListResponse listResponse = new ListResponse(HttpStatus.OK.value(),
                memberService.getNameById(memberId) + "님이 즐겨찾기한 공원 목록입니다.",
                favoriteParkList);
        return ResponseEntity.ok(listResponse);
    }

    @PostMapping("/favorites/{memberId}/{parkId}")
    public ResponseEntity<SingleResponse> addFavoritePark(@PathVariable Long memberId, @PathVariable Long parkId) {
        ParkResponse parkResponse = parkService.addFavoritePark(memberId, parkId);
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원이 즐겨찾기에 추가되었습니다.",
                parkResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/favorites/{memberId}/{parkId}")
    public ResponseEntity<SingleResponse> deleteFavoritePark(@PathVariable Long memberId, @PathVariable Long parkId) {
        ParkResponse parkResponse = parkService.deleteFavoritePark(memberId, parkId);
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "공원이 즐겨찾기에서 삭제되었습니다.",
                parkResponse);
        return ResponseEntity.ok(response);
    }
}
