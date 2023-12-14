package com.walkbuddies.backend.service.commonservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;
import org.springframework.stereotype.Service;

@Service
public interface CommonService {
  /**
   * 경위도 tm좌표 변환
   * @param x 경도
   * @param y 위도
   * @return
   * @throws URISyntaxException
   * @throws JsonProcessingException
   */
  double[] GeoToTm(double x, double y) throws URISyntaxException, JsonProcessingException;

}
