package com.walkbuddies.backend.controller.parkservice;

import com.walkbuddies.backend.dto.parkservice.ParkDto;
import com.walkbuddies.backend.service.parkservice.ParkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/park")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;

    @GetMapping("/update")
    public ResponseEntity<String> updateParkData() {
        int pageNo = 1;
        int numOfRows = 1000;

        try {
            while (true) {
                String apiUrl = parkService.buildParkAPIUrl(pageNo, numOfRows);
                String response = parkService.fetchDataFromApi(apiUrl);

                if (!parkService.hasMoreData(response)) {
                    log.info("No more data received.");
                    return ResponseEntity.ok("Park data updated successfully.");
                }

                List<ParkDto> parkDtoList = parkService.parseApiResponse(response);
                parkService.saveAllParks(parkDtoList);

                log.info(String.valueOf(pageNo));
                pageNo++;
            }
        } catch (IOException | URISyntaxException e) {
            log.error("Error during park data update", e);
            return ResponseEntity.status(500).body("Failed to update park data.");
        }
    }

    @GetMapping
    public ResponseEntity<List<ParkDto>> getParkList(@RequestParam(name = "longitude") float longitude, @RequestParam(name = "latitude") float latitude) {
        try {
            List<ParkDto> parkDtoList = parkService.getParkList(longitude, latitude);
            return ResponseEntity.ok(parkDtoList);
        } catch (Exception e) {
            log.error("Error while retrieving park list", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{parkId}")
    public ResponseEntity<ParkDto> getParkInfo(@PathVariable int parkId) {
        try {
            Optional<ParkDto> parkDto = parkService.getParkInfo(parkId);

            if (parkDto.isPresent()) {
                return ResponseEntity.ok(parkDto.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error while retrieving park", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> addPark(@RequestBody ParkDto parkDto) {
        try {
            parkService.addPark(parkDto);
            return ResponseEntity.ok("Park added successfully.");
        } catch (Exception e) {
            log.error("Error while adding park", e);
            return ResponseEntity.status(500).body("Failed to add park.");
        }
    }

    @PutMapping("/{parkId}")
    public ResponseEntity<String> updatePark(@PathVariable int parkId, @RequestBody ParkDto newDto) {
        try {
            parkService.updatePark(parkId, newDto);
            return ResponseEntity.ok("Park information updated successfully.");
        } catch (Exception e) {
            log.error("Error while updating park information", e);
            return ResponseEntity.status(500).body("Failed to update park information.");
        }
    }

    @DeleteMapping("/{parkId}")
    public ResponseEntity<String> deletePark(@PathVariable int parkId) {
        try {
            parkService.deletePark(parkId);
            return ResponseEntity.ok("Park deleted successfully.");
        } catch (Exception e) {
            log.error("Error while deleting park", e);
            return ResponseEntity.status(500).body("Failed to delete park.");
        }
    }

    @GetMapping("/favorites/{memberId}")
    public ResponseEntity<List<ParkDto>> getFavoriteParks(@PathVariable Long memberId) {
        try {
            List<ParkDto> favoriteParkList = parkService.getFavoritePark(memberId);
            return ResponseEntity.ok(favoriteParkList);
        } catch (Exception e) {
            log.error("Error while retrieving favorite park list", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/favorites/{memberId}/{parkId}")
    public ResponseEntity<String> addFavoritePark(@PathVariable Long memberId, @PathVariable Long parkId) {
        try {
            parkService.addFavoritePark(memberId, parkId);
            return ResponseEntity.ok("Favorite Park added successfully.");
        } catch (Exception e) {
            log.error("Error while adding favorite park", e);
            return ResponseEntity.status(500).body("Failed to add favorite park.");
        }
    }
}
