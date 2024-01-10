package com.walkbuddies.backend.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.repository.TownRepository;
import com.walkbuddies.backend.exception.impl.NotFoundTownException;
import com.walkbuddies.backend.weather.domain.WeatherShortEntity;
import com.walkbuddies.backend.weather.repository.WeatherShortRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherShortServiceImpl implements WeatherShortService{

    private final GpsTransfer gpsTransfer;
    private final ObjectMapper objectMapper;
    private final WeatherShortRepository weatherShortRepository;
    private final TownRepository townRepository;

    /**
     * 단기예보 api parser
     *
     * @param jsonString
     * @return
     * @throws JsonProcessingException
     */
    public JsonNode jsonParser(String jsonString) throws JsonProcessingException {

        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode itemsArray = jsonNode
                .path("response")
                .path("body")
                .path("items")
                .path("item");

        return itemsArray;
    }

    @Value("${spring.keys.api-key}")
    private String key;

    /**
     * OpenApi를 통해서 단기예보 데이터를 받아오는 메서드
     *
     * @param x
     * @param y
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Object getWeatherShortData(Double x, Double y) throws JsonProcessingException {

        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String baseTime = time.format(formatter);

        GpsTransfer.LatXLngY latXLngY = gpsTransfer.convertGRID_GPS(0, x, y);
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?" +
                "serviceKey=" + key +
                "&pageNo=1&numOfRows=1000&dataType=JSON" +
                "&base_date=" + baseTime +
                "&base_time=0500" +
                "&nx=" + Integer.parseInt(String.valueOf(Math.round(latXLngY.x))) +
                "&ny=" + Integer.parseInt(String.valueOf(Math.round(latXLngY.y)));
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        JsonNode itemsArray  = jsonParser(jsonString);

        return itemsArray;
    }

    /**
     * 위경도를 입력하면 그 위경도에 해당하는 지역의 단기예보를 저장하는 메서드
     *
     * @param x
     * @param y
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public String update(Double x, Double y) throws JsonProcessingException {

        String address = getAddress(x, y);
        Optional<TownEntity> townEntity = townRepository.findByTownName(address);
        if (townEntity.isEmpty()) {
            throw new NotFoundTownException();
        }
        TownEntity town = townEntity.get();

        JsonNode itemsArray = (JsonNode) getWeatherShortData(x, y);

        // fcstDate와 fcstTime을 기반으로 WeatherShortEntityBuilder를 저장하는 Map
        Map<String, WeatherShortEntity.WeatherShortEntityBuilder> weatherBuilderMap = new HashMap<>();

        for (JsonNode item : itemsArray) {
            String fcstDate = item.path("fcstDate").asText();
            String fcstTime = item.path("fcstTime").asText();
            String key = fcstDate + fcstTime;

            WeatherShortEntity.WeatherShortEntityBuilder weatherBuilder = weatherBuilderMap.get(key);

            if (weatherBuilder == null) {
                // 데이터베이스에서 이미 해당 키에 해당하는 엔티티가 있는지 확인
                Optional<WeatherShortEntity> existingEntity = weatherShortRepository.findByTownAndFcstDateAndFcstTime(town, fcstDate, fcstTime);
                if (existingEntity.isPresent()) {
                    // 기존 엔티티를 가져와서 업데이트
                    WeatherShortEntity existingEntityToUpdate = existingEntity.get();
                    weatherBuilder = WeatherShortEntity.builder()
                            .shortTermId(existingEntityToUpdate.getShortTermId())
                            .town(existingEntityToUpdate.getTown())
                            .nx(existingEntityToUpdate.getNx())
                            .ny(existingEntityToUpdate.getNy())
                            .baseDate(existingEntityToUpdate.getBaseDate())
                            .baseTime(existingEntityToUpdate.getBaseTime())
                            .fcstDate(existingEntityToUpdate.getFcstDate())
                            .fcstTime(existingEntityToUpdate.getFcstTime())
                            .regDate(existingEntityToUpdate.getRegDate())
                            .modDate(LocalDateTime.now());
                } else {
                    // 새로운 엔티티를 생성
                    weatherBuilder = WeatherShortEntity.builder()
                            .town(town)
                            .nx(item.path("nx").asInt())
                            .ny(item.path("ny").asInt())
                            .baseDate(item.path("baseDate").asText())
                            .baseTime(item.path("baseTime").asText())
                            .fcstDate(fcstDate)
                            .fcstTime(fcstTime)
                            .regDate(LocalDateTime.now());
                }

                weatherBuilderMap.put(key, weatherBuilder);
            }

            String category = item.path("category").asText();
            switch (category) {
                case "TMP":
                    weatherBuilder.tmp(item.path("fcstValue").asInt());
                    break;
                case "WSD":
                    weatherBuilder.wsd(item.path("fcstValue").asDouble());
                    break;
                case "SKY":
                    weatherBuilder.sky(item.path("fcstValue").asInt());
                    break;
                case "PYT":
                    weatherBuilder.pyt(item.path("fcstValue").asInt());
                    break;
                case "POP":
                    weatherBuilder.pop(item.path("fcstValue").asDouble());
                    break;
                case "PCP":
                    weatherBuilder.pcp(item.path("fcstValue").asText());
                    break;
                case "SNO":
                    weatherBuilder.sno(item.path("fcstValue").asText());
                    break;
                case "TMN":
                    weatherBuilder.tmn(item.path("fcstValue").asInt());
                    break;
                case "TMX":
                    weatherBuilder.tmx(item.path("fcstValue").asInt());
                    break;
            }
        }

        List<WeatherShortEntity> weatherShortEntities = weatherBuilderMap.values().stream()
                .map(WeatherShortEntity.WeatherShortEntityBuilder::build)
                .collect(Collectors.toList());

        weatherShortRepository.saveAll(weatherShortEntities);

        return "업데이트 완료";
    }

    @Value("${spring.keys.naver-client-id}")
    private String clientId;

    @Value("${spring.keys.naver-client-secret}")
    private String clientSecret;

    /**
     * 위경도를 받아 해당 지역의 주소를 받아오는 메서드
     *
     * @param x
     * @param y
     * @return
     */
    public String getAddress(Double x, Double y) {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?" +
                "request=coordsToaddr" +
                "&coords=" + y + "," + x +
                "&sourcecrs=epsg:4326" +
                "&output=json" +
                "&orders=legalcode";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        String result = mapParser(response.getBody());

        return result;
    }

    /**
     * 네이버 지도 api parser
     *
     * @param jsonString
     * @return
     */
    private String mapParser(String jsonString) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            if (jsonNode.path("status").path("code").asInt() == 0) {
                JsonNode resultsNode = jsonNode.path("results").get(0).path("region");

                String area1 = resultsNode.path("area1").path("name").asText();
                String area2 = resultsNode.path("area2").path("name").asText();
                String area3 = resultsNode.path("area3").path("name").asText();

                StringBuilder result = new StringBuilder();
                result.append(area1).append(" ").append(area2).append(" ").append(area3);

                return result.toString();
            } else {
                String errorMessage = jsonNode.path("status").path("message").asText();
                log.error("Error: " + errorMessage);
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response", e);
        }

        throw new RuntimeException("Error processing JSON response");
    }

}
