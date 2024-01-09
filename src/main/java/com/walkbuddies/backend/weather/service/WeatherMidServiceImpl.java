package com.walkbuddies.backend.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkbuddies.backend.weather.domain.WeatherMidEntity;
import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import com.walkbuddies.backend.weather.dto.WeatherMidLandFcstDto;
import com.walkbuddies.backend.weather.dto.WeatherMidResponse;
import com.walkbuddies.backend.weather.dto.WeatherMidTaDto;
import com.walkbuddies.backend.weather.repository.WeatherMidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WeatherMidServiceImpl implements WeatherMidService {

    private final ObjectMapper objectMapper;
    private final WeatherMidRepository weatherMidRepository;

    /**
     * jsonString을 파싱하는 메서드
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

        // 배열에서 첫 번째 객체를 가져오기
        JsonNode firstItem = itemsArray.isArray() && itemsArray.size() > 0 ? itemsArray.get(0) : null;

        return firstItem;
    }

    @Value("${spring.keys.api-key}")
    private String key;

    /**
     * 주간 최저, 최고 기온을 Oepn API를 통해 데이터를 가져오는 메서드
     *
     * @param regId
     * @param tmFc
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Object getWeatherMidTa(String regId, String tmFc) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?" +
                "serviceKey=" + key +
                "&numOfRows=10&dataType=json&pageNo=1" +
                "&regId=" + regId +
                "&tmFc=" + tmFc;
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        JsonNode firstItem = jsonParser(jsonString);
        Object result = objectMapper.treeToValue(firstItem, WeatherMidTaDto.class);

        return result;
    }

    /**
     * 중기기온조회와 중기육상예보조회의 지역코드가 다르기 때문에
     * 중기기온조회의 지역코드를 기반으로 중기육상예보조회의 지역코드를 변환해주는 메서드
     *
     * @param regId
     * @return
     */
    public String changeRegId(String regId) {
        String prefix = regId.substring(0, 4);
        String regIdForMidFcst;
        // 지역코드를 기반으로 기상예보 구역 코드 생성
        if (prefix.equals("11B0") || prefix.equals("11B1") || prefix.equals("11A0") || prefix.equals("11B2")) {
            // 서울, 인천, 경기도
            // "11A0-" 은 백령도로 인천시이다.
            regIdForMidFcst = "11B00000";
        } else if (prefix.equals("21F1") || prefix.equals("21F2")) {
            // 전라북도
            regIdForMidFcst = "11F10000";
        } else {
            // 이 외에는 앞 4글자 + "0000"
            regIdForMidFcst = prefix + "0000";
        }
        return regIdForMidFcst;
    }

    /**
     * 주간 오전, 오후 강수 확률과 날씨예보를 Open API를 통해 데이터를 가져오는 메서드
     *
     * @param regId
     * @param tmFc
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Object getWeatherMidLandFcst(String regId, String tmFc) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String changeRegId = changeRegId(regId);
        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?" +
                "serviceKey=" + key +
                "&numOfRows=10&dataType=json&pageNo=1" +
                "&regId=" + changeRegId +
                "&tmFc=" + tmFc;
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        JsonNode firstItem = jsonParser(jsonString);
        Object result = objectMapper.treeToValue(firstItem, WeatherMidLandFcstDto.class);

        return result;
    }

    /**
     * 설정된 시간에 자동으로 지역코드를 기반으로
     * DB에 이미 저장되어있는 지역이라면 업데이트하고 없는 지역이라면 새로 추가하는 메서드
     *
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    @Scheduled(cron = "0 0 6,18 * * ?")
    @Override
    public void autoUpdate() throws JsonProcessingException {
        String tmFc = generateTmFc();
        processWeatherMidData(tmFc);
    }

    /**
     * 수동으로 지역코드를 기반으로 DB에 이미 저장되어있는 지역이라면 업데이트하고 없는 지역이라면 새로 추가하는 메서드
     * @param tmFc
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    @Override
    public void update(String tmFc) throws JsonProcessingException {
        processWeatherMidData(tmFc);
    }

    private void processWeatherMidData(String tmFc) throws JsonProcessingException {
        List<WeatherMidDto> dataFromDB = getDataFromDB();
        for (WeatherMidDto weatherMidDto : dataFromDB) {
            WeatherMidTaDto weatherMidTa = (WeatherMidTaDto) getWeatherMidTa(weatherMidDto.getRegId(), tmFc);
            WeatherMidLandFcstDto weatherMidLandFcst = (WeatherMidLandFcstDto) getWeatherMidLandFcst(weatherMidDto.getRegId(), tmFc);

            if (weatherMidTa == null || weatherMidLandFcst == null) {
                // null 값이 오면 해당 도시의 업데이트를 무시하고 다음 도시로 진행
                continue;
            }

            Optional<WeatherMidEntity> optionalWeatherMidEntity = weatherMidRepository.findByRegId(weatherMidDto.getRegId());

            if (optionalWeatherMidEntity.isPresent()) {
                updateExistingEntity(optionalWeatherMidEntity.get(), tmFc, weatherMidTa, weatherMidLandFcst);
            } else {
                saveNewEntity(weatherMidDto, tmFc, weatherMidTa, weatherMidLandFcst);
            }
        }
    }

    private void updateExistingEntity(WeatherMidEntity existingEntity, String tmFc, WeatherMidTaDto weatherMidTa, WeatherMidLandFcstDto weatherMidLandFcst) {
        WeatherMidEntity updatedEntity = new WeatherMidEntity(
                existingEntity.getMidTermId(),
                existingEntity.getRegId(),
                existingEntity.getCityName(),
                tmFc,
                weatherMidTa.getTaMin3(),
                weatherMidTa.getTaMax3(),
                weatherMidTa.getTaMin4(),
                weatherMidTa.getTaMax4(),
                weatherMidTa.getTaMin5(),
                weatherMidTa.getTaMax5(),
                weatherMidTa.getTaMin6(),
                weatherMidTa.getTaMax6(),
                weatherMidTa.getTaMin7(),
                weatherMidTa.getTaMax7(),
                weatherMidLandFcst.getRnSt3Am(),
                weatherMidLandFcst.getRnSt3Pm(),
                weatherMidLandFcst.getRnSt4Am(),
                weatherMidLandFcst.getRnSt4Pm(),
                weatherMidLandFcst.getRnSt5Am(),
                weatherMidLandFcst.getRnSt5Pm(),
                weatherMidLandFcst.getRnSt6Am(),
                weatherMidLandFcst.getRnSt6Pm(),
                weatherMidLandFcst.getRnSt7Am(),
                weatherMidLandFcst.getRnSt7Pm(),
                weatherMidLandFcst.getWf3Am(),
                weatherMidLandFcst.getWf3Pm(),
                weatherMidLandFcst.getWf4Am(),
                weatherMidLandFcst.getWf4Pm(),
                weatherMidLandFcst.getWf5Am(),
                weatherMidLandFcst.getWf5Pm(),
                weatherMidLandFcst.getWf6Am(),
                weatherMidLandFcst.getWf6Pm(),
                weatherMidLandFcst.getWf7Am(),
                weatherMidLandFcst.getWf7Pm(),
                existingEntity.getRegDate(),
                LocalDateTime.now()
        );

        weatherMidRepository.save(updatedEntity);
    }

    private void saveNewEntity(WeatherMidDto weatherMidDto, String tmFc, WeatherMidTaDto weatherMidTa, WeatherMidLandFcstDto weatherMidLandFcst) {
        WeatherMidEntity weatherMidEntity = WeatherMidEntity.builder()
                .regId(weatherMidDto.getRegId())
                .cityName(weatherMidDto.getCityName())
                .tmFc(tmFc)
                .taMin3(weatherMidTa.getTaMin3())
                .taMax3(weatherMidTa.getTaMax3())
                .taMin4(weatherMidTa.getTaMin4())
                .taMax4(weatherMidTa.getTaMax4())
                .taMin5(weatherMidTa.getTaMin5())
                .taMax5(weatherMidTa.getTaMax5())
                .taMin6(weatherMidTa.getTaMin6())
                .taMax6(weatherMidTa.getTaMax6())
                .taMin7(weatherMidTa.getTaMin7())
                .taMax7(weatherMidTa.getTaMax7())
                .rnSt3Am(weatherMidLandFcst.getRnSt3Am())
                .rnSt3Pm(weatherMidLandFcst.getRnSt3Pm())
                .rnSt4Am(weatherMidLandFcst.getRnSt4Am())
                .rnSt4Pm(weatherMidLandFcst.getRnSt4Pm())
                .rnSt5Am(weatherMidLandFcst.getRnSt5Am())
                .rnSt5Pm(weatherMidLandFcst.getRnSt5Pm())
                .rnSt6Am(weatherMidLandFcst.getRnSt6Am())
                .rnSt6Pm(weatherMidLandFcst.getRnSt6Pm())
                .rnSt7Am(weatherMidLandFcst.getRnSt7Am())
                .rnSt7Pm(weatherMidLandFcst.getRnSt7Pm())
                .wf3Am(weatherMidLandFcst.getWf3Am())
                .wf3Pm(weatherMidLandFcst.getWf3Pm())
                .wf4Am(weatherMidLandFcst.getWf4Am())
                .wf4Pm(weatherMidLandFcst.getWf4Pm())
                .wf5Am(weatherMidLandFcst.getWf5Am())
                .wf5Pm(weatherMidLandFcst.getWf5Pm())
                .wf6Am(weatherMidLandFcst.getWf6Am())
                .wf6Pm(weatherMidLandFcst.getWf6Pm())
                .wf7Am(weatherMidLandFcst.getWf7Am())
                .wf7Pm(weatherMidLandFcst.getWf7Pm())
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();

        weatherMidRepository.save(weatherMidEntity);
    }

    private String generateTmFc() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        return now.format(formatter);
    }

    /**
     * DB에서 도시이름에 해당하는 중기예보 데이터를 가져오는 메서드
     *
     * @param cityName
     * @return
     */
    public List<WeatherMidResponse> getData(String cityName) {

        WeatherMidEntity result = weatherMidRepository.findByCityName(cityName)
                .orElseThrow(() -> new NoSuchElementException("도시 이름이 없습니다: " + cityName));

        List<WeatherMidResponse> weatherMidResponses = List.of(WeatherMidResponse.of(result));

        return weatherMidResponses;
    }

    /**
     * DB에서 도시이름, 지역코드를 가져와서 List<WeatherMidDto>에 담아 리턴하는 메서드
     *
     * @return
     */
    public List<WeatherMidDto> getDataFromDB() {
        List<WeatherMidEntity> weatherMidEntities = weatherMidRepository.findAll();
        List<WeatherMidDto> weatherMidDtos = new ArrayList<>();

        for (WeatherMidEntity entity : weatherMidEntities) {
            WeatherMidDto weatherMidDto = WeatherMidDto.builder()
                    .cityName(entity.getCityName())
                    .regId(entity.getRegId())
                    .build();

            weatherMidDtos.add(weatherMidDto);
        }

        return weatherMidDtos;
    }
}
