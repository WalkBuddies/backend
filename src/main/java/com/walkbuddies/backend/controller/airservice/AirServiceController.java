package com.walkbuddies.airservice.controller.airService;

import com.walkbuddies.backend.domain.airservice.AirServiceEntity;
import com.walkbuddies.airservice.service.airService.AirService;

import java.io.IOException;
import java.net.URISyntaxException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/air")
@AllArgsConstructor
@RestController
public class AirServiceController {
  private final AirService airService;

  @GetMapping("/data")
  public ResponseEntity<?> getAirInfo(@RequestParam double tmX, @RequestParam double tmY)
      throws IOException, URISyntaxException {
    AirServiceEntity result = this.airService.getAirInfo(tmX, tmY);
    return ResponseEntity.ok(result);
  }


}
