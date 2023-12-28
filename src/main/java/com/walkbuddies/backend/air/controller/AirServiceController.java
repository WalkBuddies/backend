package com.walkbuddies.backend.air.controller;

import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.air.service.AirService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RequestMapping("/air")
@AllArgsConstructor
@RestController
public class AirServiceController {
    private final AirService airService;

    @GetMapping("/data")
    public ResponseEntity<?> getAirInfo(@RequestParam double x, @RequestParam double y)
            throws IOException, URISyntaxException {
        AirServiceDto result = this.airService.getAirInfo(x, y);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/bookmark-data")
    public ResponseEntity<?> getBookmarkAirInfo(@RequestParam double x, @RequestParam double y)
            throws URISyntaxException, IOException {
        AirServiceDto result = this.airService.getBookmarkAirInfo(x, y);
        return ResponseEntity.ok(result);
    }

}
