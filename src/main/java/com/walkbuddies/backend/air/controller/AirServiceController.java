package com.walkbuddies.backend.air.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.air.service.AirService;
import com.walkbuddies.backend.common.response.SingleResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@Slf4j
@RequestMapping("/air")
@AllArgsConstructor
@RestController
public class AirServiceController {
    private final AirService airService;

    @GetMapping("/data")
    public ResponseEntity<SingleResponse<AirServiceDto>> getAirInfo(@RequestParam double x, @RequestParam double y)
        throws URISyntaxException, JsonProcessingException {
        AirServiceDto result = this.airService.getAirInfo(x, y);
        SingleResponse<AirServiceDto> response = new SingleResponse<>(HttpStatus.OK.value(), "조회 완료", result);
        return ResponseEntity.ok(response);
    }


}
