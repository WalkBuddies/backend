package com.walkbuddies.backend.bookmark.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkbuddies.backend.air.dto.AirServiceDto;
import com.walkbuddies.backend.air.service.AirService;
import com.walkbuddies.backend.bookmark.domain.BookmarkEntity;
import com.walkbuddies.backend.bookmark.dto.BookmarkDto;
import com.walkbuddies.backend.bookmark.dto.BookmarkParameter;
import com.walkbuddies.backend.bookmark.repository.BookmarkRepository;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.repository.TownRepository;
import com.walkbuddies.backend.exception.impl.*;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import com.walkbuddies.backend.weather.dto.WeatherMidDto;
import com.walkbuddies.backend.weather.service.WeatherMidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService{

    private final TownRepository townRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final WeatherMidService weatherMidService;
    private final AirService airService;

    /**
     * 동네 즐겨찾기 등록 메서드
     * @param bookmarkParameter
     * @return
     */
    @Transactional
    @Override
    public BookmarkDto bookmarkRedister(BookmarkParameter bookmarkParameter) {

        TownEntity townEntity = townRepository.findByTownName(bookmarkParameter.getTownName()).orElse(null);
        if (townEntity == null) {
            throw new NotFoundTownException();
        }

        MemberEntity memberEntity = getMemberEntity(bookmarkParameter.getMemberId());

        Optional<BookmarkEntity> optionalBookmarkEntity = bookmarkRepository.findByTownIdAndMemberId(townEntity, memberEntity);
        if (optionalBookmarkEntity.isPresent()) {
            throw new ExisitsBookmarkException();
        }

        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .townId(townEntity)
                .memberId(memberEntity)
                .bookmarkName(bookmarkParameter.getBookmarkName())
                .regDate(LocalDateTime.now())
                .build();
       bookmarkRepository.save(bookmarkEntity);

        return BookmarkEntity.entityToDto(bookmarkEntity);
    }

    /**
     * 내 즐겨찾기 불러오기 메서드
     * @param memberId
     * @return
     */
    @Override
    public List<BookmarkDto> getMyBookmark(Long memberId) {

        MemberEntity memberEntity = getMemberEntity(memberId);

        Optional<List<BookmarkEntity>> optionalBookmarkEntities = bookmarkRepository.findByMemberId(memberEntity);
        if (optionalBookmarkEntities.isEmpty()) {
            throw new NotFoundBookmarkException();
        }

        List<BookmarkDto> bookmarkDtos = new ArrayList<>();
        for (BookmarkEntity bookmarkEntity : optionalBookmarkEntities.get()) {
            bookmarkDtos.add(BookmarkEntity.entityToDto(bookmarkEntity));
        }

        return bookmarkDtos;
    }

    /**
     * 즐겨찾기 삭제 메서드
     * @param bookmarkId
     * @return
     */
    @Transactional
    @Override
    public BookmarkDto bookmarkDelete(Long bookmarkId) {

        BookmarkEntity bookmarkEntity = getBookmarkEntity(bookmarkId);
        bookmarkRepository.delete(bookmarkEntity);

        return BookmarkEntity.entityToDto(bookmarkEntity);
    }

    /**
     * 즐겨찾기 수정 메서드
     * @param bookmarkId
     * @param bookmarkName
     * @return
     */
    @Transactional
    @Override
    public BookmarkDto bookmarkUpdate(Long bookmarkId, String bookmarkName) {

        BookmarkEntity bookmarkEntity = getBookmarkEntity(bookmarkId);
        if (bookmarkEntity.getBookmarkName().equals(bookmarkName)) {
            throw new DuplicateNameException();
        }

        BookmarkEntity updatedBookmarkEntity = BookmarkEntity.builder()
                .bookmarkId(bookmarkEntity.getBookmarkId())
                .townId(bookmarkEntity.getTownId())
                .memberId(bookmarkEntity.getMemberId())
                .bookmarkName(bookmarkName)
                .regDate(bookmarkEntity.getRegDate())
                .build();
        bookmarkRepository.save(updatedBookmarkEntity);

        return BookmarkEntity.entityToDto(updatedBookmarkEntity);
    }

    /**
     * 즐겨찾기 지역 중기예보 정보를 가져오는 메서드
     * @param bookmarkId
     * @return
     */
    @Override
    public List<WeatherMidDto> getWeatheMidData(Long bookmarkId) {

        BookmarkEntity bookmarkEntity = getBookmarkEntity(bookmarkId);
        TownEntity townEntity = getTownEntity(bookmarkEntity.getTownId().getTownId());

        String cityName = townEntity.getTownName();
        if ((cityName.contains("광역시") || cityName.contains("특별시") || cityName.contains("특별자치시"))) {
            cityName = cityName.substring(0, 2);
        } else {
            cityName = cityName.split("\\s")[1];
            if (cityName.length() > 1) {
                cityName = cityName.substring(0, cityName.length() - 1);
            }
        }

        return weatherMidService.getWeatherMidData(cityName);
    }

    /**
     * 즐겨 찾기 지역 미세먼지 정보를 가져오는 메서드
     * @param bookmarkId
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @Override
    public AirServiceDto getAirData(Long bookmarkId) throws IOException, URISyntaxException {

        BookmarkEntity bookmarkEntity = getBookmarkEntity(bookmarkId);
        TownEntity townEntity = getTownEntity(bookmarkEntity.getTownId().getTownId());

        String result = getCoordinate(townEntity.getTownName());
        double x = Double.parseDouble(result.split("\\s")[0]);
        double y = Double.parseDouble(result.split("\\s")[1]);

        return airService.getAirInfo(x, y);
    }

    @Value("${spring.keys.naver-client-id}")
    private String clientId;

    @Value("${spring.keys.naver-client-secret}")
    private String clientSecret;

    public String getCoordinate(String address) {

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        String result = jsonParser(response.getBody());

        return result;
    }

    private String jsonParser(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            if ("OK".equals(jsonNode.path("status").asText())) {
                JsonNode addressesNode = jsonNode.path("addresses");
                JsonNode addressNode = addressesNode.get(0);

                String x = addressNode.path("x").asText();
                String y = addressNode.path("y").asText();

                StringBuilder result = new StringBuilder();
                result.append(x).append(" ").append(y);

                return result.toString();
            } else {
                String errorMessage = jsonNode.path("errorMessage").asText();
                log.error("Error: " + errorMessage);
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response", e);
        }

        throw new RuntimeException("Error processing JSON response");
    }

    private MemberEntity getMemberEntity(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundMemberException());
    }

    private BookmarkEntity getBookmarkEntity(Long bookmarkId) {
        return bookmarkRepository.findByBookmarkId(bookmarkId)
                .orElseThrow(() -> new NotFoundBookmarkException());
    }

    private TownEntity getTownEntity(Long townId) {
        return townRepository.findByTownId(townId)
                .orElseThrow(() -> new NotFoundTownException());
    }
}
