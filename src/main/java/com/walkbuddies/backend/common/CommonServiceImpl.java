package com.walkbuddies.backend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonServiceImpl implements CommonService {
    private final ObjectMapper objectMapper;
    @Value("${spring.keys.kakao-api-key}")
    private String KAKAO_API_KEY;

    @Override
    public double[] GeoToTm(double x, double y) throws URISyntaxException, JsonProcessingException {
        String url = "https://dapi.kakao.com/v2/local/geo/transcoord.json" + "?output_coord=TM" + "&x=" + x + "&y=" + y;
        double[] result = new double[2];
        URI uri = new URI(url);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + KAKAO_API_KEY);
        RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        JsonNode item = jsonNode.path("documents");

        result[0] = Double.parseDouble(String.valueOf(item.get(0).get("x")));
        result[1] = Double.parseDouble(String.valueOf(item.get(0).get("y")));

        log.info("kakao 좌표변환 완료");

        return result;
    }

}
