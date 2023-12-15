package com.walkbuddies.backend.controller.parkservice;

import com.walkbuddies.backend.dto.parkservice.ParkDto;
import com.walkbuddies.backend.service.parkservice.ParkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/park")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;

    @GetMapping("/update")
    public ResponseEntity<String> updateParkData() {
        parkService.updateParkData();
        return ResponseEntity.ok("Park data updated successfully.");
    }

    @GetMapping
    public ResponseEntity<List<ParkDto>> getParkList(@RequestParam(name = "longitude") Double longitude, @RequestParam(name = "latitude") Double latitude) {
        List<ParkDto> parkDtoList = parkService.getParkList(longitude, latitude);
        return ResponseEntity.ok(parkDtoList);
    }

    @GetMapping("/{parkId}")
    public ResponseEntity<ParkDto> getParkInfo(@PathVariable Long parkId) {
        ParkDto parkDto = parkService.getParkInfo(parkId);
        return ResponseEntity.ok(parkDto);
    }

    @PostMapping
    public ResponseEntity<String> addPark(@RequestBody ParkDto parkDto) {
        parkService.addPark(parkDto);
        return ResponseEntity.ok("Park added successfully.");
    }

    @PutMapping("/{parkId}")
    public ResponseEntity<String> updatePark(@PathVariable Long parkId, @RequestBody ParkDto newDto) {
        parkService.updatePark(parkId, newDto);
        return ResponseEntity.ok("Park information updated successfully.");
    }

    @DeleteMapping("/{parkId}")
    public ResponseEntity<String> deletePark(@PathVariable Long parkId) {
        parkService.deletePark(parkId);
        return ResponseEntity.ok("Park deleted successfully.");
    }

    @GetMapping("/favorites/{memberId}")
    public ResponseEntity<List<ParkDto>> getFavoriteParks(@PathVariable Long memberId) {
        List<ParkDto> favoriteParkList = parkService.getFavoritePark(memberId);
        return ResponseEntity.ok(favoriteParkList);
    }

    @PostMapping("/favorites/{memberId}/{parkId}")
    public ResponseEntity<String> addFavoritePark(@PathVariable Long memberId, @PathVariable Long parkId) {
        parkService.addFavoritePark(memberId, parkId);
        return ResponseEntity.ok("Favorite Park added successfully.");
    }

    @DeleteMapping("/favorites/{memberId}/{parkId}")
    public ResponseEntity<String> deleteFavoritePark(@PathVariable Long memberId, @PathVariable Long parkId) {
        parkService.deleteFavoritePark(memberId, parkId);
        return ResponseEntity.ok("Favorite Park deleted successfully.");
    }
}
