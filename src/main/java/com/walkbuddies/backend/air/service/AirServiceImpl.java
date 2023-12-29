package com.walkbuddies.backend.air.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.walkbuddies.backend.air.domain.AirServiceEntity;
import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.air.dto.MsrstnDto;
import com.walkbuddies.backend.air.repository.AirServiceRepository;
import com.walkbuddies.backend.common.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class AirServiceImpl implements AirService {
    @Value("${spring.keys.api-key}")
    private String API_KEY;
    private final ObjectMapper objectMapper;
    final AirServiceRepository airServiceRepository;
    private final CommonService commonService;
    private final RedisTemplate<String, AirServiceDto> airRedisTemplate;

    /**
     * api dataTime 항목을 dateTimeFormat으로 변환하는 메소드
     *
     * @param time 문자열 dateTime
     * @return dateTime
     */
    private String changeTimeFormat(String time) {
        time = time.substring(1, time.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return dateTime.toString();
    }

    /**
     * api 데이터 받아오는 메소드
     *
     * @param apiUrl
     * @return 통신결과 jsonString 리턴
     *
     */
    public String getApiInfo(String apiUrl) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(apiUrl);

        return restTemplate.getForObject(uri, String.class);
    }


    /**
     * jsonString을 파싱하는 메소드
     *
     * @param jsonString
     * @return
     * @throws JsonProcessingException
     */
    private JsonNode jsonParser(String jsonString) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        return jsonNode
                .path("response")
                .path("body")
                .path("items");
    }

    private static final String REDIS_KEY_PREFIX = "air:stationName:";

    /**
     * (수정진행중)
     * 요청좌표의 미세먼지정보를 찾는 메소드
     * 최근접측정소를 찾은 후 측정소 코드를 통해 db에 저장된 정보가 없다면 미세먼지 api에서 정보를 받음
     * db에 저장된 정보가 조회시간보다 1시간 이내면 db의 정보를 반환, 아니면 api에서 정보를 받음
     *
     * @param X 경도
     * @param Y 위도
     * @return
     * @throws IOException
     */
    public AirServiceDto getAirInfo(double X, double Y)
            throws URISyntaxException, JsonProcessingException {
      
        double[] tm = commonService.GeoToTm(X, Y);
        MsrstnDto msrstnDto = getNearbyMsrstnInfoFromApi(tm[0], tm[1]);

        AirServiceDto result ;
        LocalDateTime now = LocalDateTime.now();

        String redisKey = REDIS_KEY_PREFIX + msrstnDto.getStationName();

        // Redis에서 데이터 조회
        AirServiceDto cachedData = airRedisTemplate.opsForValue().get(redisKey);

        if (cachedData != null) {
            // Redis에 데이터가 존재하면 반환
            return cachedData;
        } else {
            // Redis에 데이터가 없으면 DB 또는 API에서 데이터 가져오기
            Optional<AirServiceEntity> checkDb = airServiceRepository.findByStationCode(msrstnDto.getStationCode());
            if (checkDb.isPresent()) {
                AirServiceEntity airServiceEntity = checkDb.get();
                if (airServiceEntity.getDataTime().isBefore(now.minusHours(1))) {
                    AirServiceEntity data = getAirInfoFromApi(msrstnDto);
                    saveApiData(data);
                    result = AirServiceEntity.entityToDto(data);
                } else {
                    result = AirServiceEntity.entityToDto(airServiceEntity);
                }
            } else {
                AirServiceEntity data = getAirInfoFromApi(msrstnDto);
                saveApiData(data);
                result = AirServiceEntity.entityToDto(data);
            }
        }

        return result;
    }

    /**
     * 요청받은 좌표로부터 최근접측정소를 찾는 api 호출
     *
     * @param tmX 입력받은 tmx 좌표
     * @param tmY 입력받은 tmy 좌표
     * @return 최근접측정소 정보(msrstnDto) 리턴
     * @throws
     */
    public MsrstnDto getNearbyMsrstnInfoFromApi(double tmX, double tmY)
            throws JsonProcessingException, URISyntaxException {
        String apiUrl = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?"
                + "tmX=" + tmX
                + "&tmY=" + tmY
                + "&returnType=json"
                + "&ver=1.1"
                + "&serviceKey=" + API_KEY;
        String result = getApiInfo(apiUrl);
        JsonNode items = jsonParser(result);
        log.info("근접측정소 api 조회 완료");
        return objectMapper.treeToValue(items.get(0), MsrstnDto.class);
    }

    /**
     * 미세먼지 api 조회
     *
     * @param msrstnDto 측정소정보 dto
     * @return
     * @throws
     */
    public AirServiceEntity getAirInfoFromApi(MsrstnDto msrstnDto)
            throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        UriComponents complexUrl = UriComponentsBuilder
                .fromUriString("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
                .queryParam("dataTerm", "daily")
                .queryParam("returnType", "json")
                .queryParam("ver", "1.4")
                .queryParam("serviceKey", API_KEY)
                .queryParam("stationName", msrstnDto.getStationName()).encode().build();
        String result = restTemplate.getForObject(complexUrl.toUri(), String.class);
        JsonNode items = jsonParser(result);
        AirServiceEntity airServiceEntity = new AirServiceEntity();
        for (int i = 0; i < items.size(); i++) {
            JsonNode item = items.get(i);
            if (!item.get("coFlag").isNull() || !item.get("pm10Flag").isNull()) {
                log.warn("통신장애");
                continue;
            }
            ((ObjectNode) item).put("dataTime", changeTimeFormat(String.valueOf(item.get("dataTime"))));
            airServiceEntity = objectMapper.treeToValue(item, AirServiceEntity.class);
            log.info(airServiceEntity.getStationName() + "저장 완료");
            break;
        }

        return airServiceEntity;
    }

    /**
     * api 데이터 저장
     *
     * @param airServiceEntity
     */
    private void saveApiData(AirServiceEntity airServiceEntity) {

        // Redis key
        String redisKey = REDIS_KEY_PREFIX + airServiceEntity.getStationCode();

        // 데이터를 Redis에 저장 (유효시간은 1시간으로 설정)
        airRedisTemplate.opsForValue().set(redisKey, AirServiceEntity.entityToDto(airServiceEntity), Duration.ofHours(1));

        airServiceRepository.save(airServiceEntity);
        log.info("대기정보 저장 완료: " + airServiceEntity.getStationName());
    }

}
