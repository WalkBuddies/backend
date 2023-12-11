package com.walkbuddies.airservice.service.airService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.walkbuddies.backend.domain.airservice.AirServiceEntity;
import com.walkbuddies.backend.dto.airservice.MsrstnDto;
import com.walkbuddies.airservice.repository.airService.AirServiceRepository;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class AirServiceImpl implements AirService {
  @Value("${spring.keys.api-key}")
  private String API_KEY;
  private final ObjectMapper objectMapper;
  final AirServiceRepository airServiceRepository;

  /**
   * api dataTime 항목을 datetimeformat으로 변환하는 메소드
   * @param time 문자열 dateTime
   * @return dateTime
   */
  private String changeTimeFormat(String time) {
    time = time.substring(1, time.length() - 1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime dateTime = LocalDateTime.parse(time,formatter);
    String result = dateTime.toString();
    return result;
  }

  /**
   * api 데이터 받아오는 메소드
   * @param apiUrl
   * @return 통신결과 jsonString 리턴
   * @throws IOException
   */
  public String getApiInfo(String apiUrl) throws URISyntaxException {

    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(apiUrl);
    String result = restTemplate.getForObject(uri,String.class);

    return result;
  }

  /**
   * jsonString을 파싱하는 메소드
   * @param jsonString
   * @return
   * @throws JsonProcessingException
   */
  private JsonNode jsonParser(String jsonString) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(jsonString);
    JsonNode itemsArray = jsonNode
        .path("response")
        .path("body")
        .path("items");

    return itemsArray;
  }

  /**
   * 요청좌표의 미세먼지정보를 찾는 메소드
   * 최근접측정소를 찾은 후 측정소 코드를 통해 db에 저장된 정보가 없다면 미세먼지 api에서 정보를 받음
   * db에 저장된 정보가 조회시간보다 1시간 이내면 db의 정보를 반환, 아니면 api에서 정보를 받음
   * @param tmX
   * @param tmY
   * @return
   * @throws IOException
   */
  public AirServiceEntity getAirInfo(double tmX, double tmY) throws IOException, URISyntaxException {
    MsrstnDto msrstnDto = getNearbyMsrstnInfoFromApi(tmX, tmY);
    AirServiceEntity result;
    LocalDateTime now = LocalDateTime.now();
    Optional<AirServiceEntity> checkDb = airServiceRepository.findByStationCode(msrstnDto.getStationCode());

    if (checkDb.isPresent()) {
      AirServiceEntity airServiceEntity = checkDb.get();
      if (airServiceEntity.getDataTime().isBefore(now.minusHours(1))) {
        result = getAirInfoFromApi(msrstnDto);
      } else {
        result = airServiceEntity;
      }
    } else {
      result = getAirInfoFromApi(msrstnDto);
    }
    return result;
  }

  /**
   * 요청받은 좌표로부터 최근접측정소를 찾는 api 호출
   * @param tmX 입력받은 tmx 좌표
   * @param tmY 입력받은 tmy 좌표
   * @return 최근접측정소 정보(msrstnDto) 리턴
   * @throws IOException
   */
  public MsrstnDto getNearbyMsrstnInfoFromApi(double tmX, double tmY)
      throws IOException, URISyntaxException {
    String apiUrl = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?"
        +"tmX=" + tmX
        +"&tmY=" + tmY
        +"&returnType=json"
        +"&ver=1.1"
        +"&serviceKey=" + API_KEY;
    String result = getApiInfo(apiUrl);
    JsonNode items = jsonParser(result);

    MsrstnDto msrtnDto = objectMapper.treeToValue(items.get(0), MsrstnDto.class);

    return msrtnDto;
  }

  /**
   * 미세먼지 api 조회
   * @param msrstnDto 측정소정보 dto
   * @return
   * @throws IOException
   */
   public AirServiceEntity getAirInfoFromApi(MsrstnDto msrstnDto)
       throws IOException {

     RestTemplate restTemplate = new RestTemplate();
     UriComponents complexUrl = UriComponentsBuilder
         .fromUriString("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
         .queryParam("dataTerm", "daily")
         .queryParam("returnType","json")
         .queryParam("ver","1.4")
         .queryParam("serviceKey","eGMtdRftJiYZQLaCrsbuga7lhzTOZ5YjKsXxB4onZefxLAEtGf49DZr1S+e6rJyiTRHtKL2J001cnCC+h52ieQ==" )
         .queryParam("stationName", msrstnDto.getStationName()).encode().build();
     String result = restTemplate.getForObject(complexUrl.toUri(), String.class);
     JsonNode items = jsonParser(result);
     AirServiceEntity airServiceEntity = new AirServiceEntity();
     for (int i = 0; i < items.size(); i++) {
       JsonNode item = items.get(i);
       if (!item.get("coFlag").isNull() || !item.get("pm10Flag").isNull()) {
           log.info("통신장애");
           continue ;
       }
       ((ObjectNode)item).put("dataTime", changeTimeFormat(String.valueOf(item.get("dataTime"))));
       airServiceEntity = objectMapper.treeToValue(item, AirServiceEntity.class);
       airServiceRepository.save(airServiceEntity);
       log.info(airServiceEntity.getStationName() + "저장 완료");
       break;
    }

    return airServiceEntity;
  }



}
