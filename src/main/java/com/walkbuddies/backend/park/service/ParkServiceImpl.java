package com.walkbuddies.backend.park.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.walkbuddies.backend.exception.impl.NotFoundFavoriteParkException;
import com.walkbuddies.backend.exception.impl.NotFoundMemberException;
import com.walkbuddies.backend.exception.impl.NotFoundParkException;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import com.walkbuddies.backend.park.domain.FavoriteParkEntity;
import com.walkbuddies.backend.park.domain.ParkEntity;
import com.walkbuddies.backend.park.dto.ParkDistanceResponse;
import com.walkbuddies.backend.park.dto.ParkRequest;
import com.walkbuddies.backend.park.dto.ParkResponse;
import com.walkbuddies.backend.park.repository.FavoriteParkRepository;
import com.walkbuddies.backend.park.repository.ParkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkServiceImpl implements ParkService {

    private final ParkRepository parkRepository;
    private final FavoriteParkRepository favoriteParkRepository;
    private final MemberRepository memberRepository;

    @Value("${park-service-key}")
    private String serviceKey;

    @Override
    @Transactional
    public void updateParkData() {
        int pageNo = 1;
        int numOfRows = 300;

        while (true) {
            String apiUrl = buildParkAPIUrl(pageNo, numOfRows);
            String response = fetchDataFromApi(apiUrl);

            if (!hasMoreData(response)) {
                log.info("No more data received.");
                break;
            }

            List<ParkRequest> parkRequestList = parseApiResponse(response);
            saveAllParks(parkRequestList);

            log.info("pageNo: " + pageNo);
            pageNo++;
        }

    }

    @Override
    public String buildParkAPIUrl(int pageNo, int numOfRows) {
        return "http://api.data.go.kr/openapi/tn_pubr_public_cty_park_info_api" +
                "?serviceKey=" + serviceKey + "&type=json" +
                "&pageNo=" + pageNo + "&numOfRows=" + numOfRows;
    }

    @Override
    public String fetchDataFromApi(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri;
        try {
            uri = new URI(apiUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return restTemplate.getForObject(uri, String.class);
    }

    @Override
    public boolean hasMoreData(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responseNode = rootNode.path("response");
        if (responseNode.isMissingNode()) {
            return false;
        }

        JsonNode headerNode = responseNode.path("header");
        if (headerNode.isMissingNode()) {
            return false;
        }

        String resultCode = headerNode.path("resultCode").asText();
        String resultMsg = headerNode.path("resultMsg").asText();

        return !"03".equals(resultCode) && !"NODATA_ERROR".equals(resultMsg);
    }

    @Override
    public List<ParkRequest> parseApiResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responseNode = rootNode.path("response");
        JsonNode bodyNode = responseNode.path("body");
        JsonNode itemsNode = bodyNode.path("items");

        if (responseNode.isMissingNode() || itemsNode.isMissingNode()) {
            return Collections.emptyList();
        }

        ArrayNode parkArrayNode = (ArrayNode) itemsNode;
        List<ParkRequest> parkRequestList = new ArrayList<>();
        for (JsonNode parkNode : parkArrayNode) {
            ParkRequest parkRequest = new ParkRequest();

            parkRequest.setParkName(parkNode.path("parkNm").asText());

            if (parkNode.has("latitude")) {
                parkRequest.setLatitude(parkNode.path("latitude").asText());
            }

            if (parkNode.has("longitude")) {
                parkRequest.setLongitude(parkNode.path("longitude").asText());
            }

            parkRequest.setAddress(parkNode.path("lnmadr").asText());

            if (parkNode.has("mvmFclty")) {
                parkRequest.setSportFacility(parkNode.path("mvmFclty").asText());
            }

            if (parkNode.has("cnvnncFclty")) {
                parkRequest.setConvenienceFacility(parkNode.path("cnvnncFclty").asText());
            }

            parkRequestList.add(parkRequest);
        }
        return parkRequestList;
    }

    @Override
    @Transactional
    public void saveAllParks(List<ParkRequest> parkRequestList) {
        for (ParkRequest parkRequest : parkRequestList) {
            Optional<ParkEntity> existingPark = parkRepository.findByAddress(parkRequest.getAddress());

            if (existingPark.isPresent()) {
                updateExistingPark(existingPark.get(), parkRequest);
            } else {
                ParkEntity newPark = ParkEntity.convertToEntity(parkRequest);
                parkRepository.save(newPark);
            }
        }
    }

    @Override
    public List<ParkDistanceResponse> getParkList(Double longitude, Double latitude) {
        List<ParkDistanceResponse> result = new ArrayList<>();

        List<Object[]> parkList = parkRepository.findNearbyParks(longitude, latitude, 1000);
        for (Object[] park : parkList) {
            ParkDistanceResponse parkDistanceResponse = new ParkDistanceResponse();
            Optional<ParkEntity> optionalPark = parkRepository.findById((Long) park[0]);
            if (optionalPark.isPresent()) {
                ParkEntity parkEntity = optionalPark.get();
                parkDistanceResponse = ParkDistanceResponse.convertToDto(parkEntity);
                parkDistanceResponse.setDistance((Double) park[1]);
            }

            result.add(parkDistanceResponse);
        }

        result.sort(Comparator.comparing(ParkDistanceResponse::getDistance));

        return result;
    }

    @Override
    public ParkResponse getParkInfo(Long parkId) {
        ParkEntity park = parkRepository.findById(parkId)
                .orElseThrow(NotFoundParkException::new);

        return ParkResponse.convertToDto(park);
    }

    @Override
    public ParkResponse addPark(ParkRequest parkRequest) {
        ParkEntity parkEntity = ParkEntity.convertToEntity(parkRequest);
        parkRepository.save(parkEntity);

        return ParkResponse.convertToDto(parkEntity);
    }

    @Override
    @Transactional
    public ParkResponse updatePark(Long parkId, ParkRequest newDto) {
        ParkEntity park = parkRepository.findById(parkId)
                .orElseThrow(NotFoundParkException::new);
        updateExistingPark(park, newDto);
        parkRepository.save(park);

        return ParkResponse.convertToDto(park);
    }

    @Override
    public ParkResponse deletePark(Long parkId) {
        ParkEntity park = parkRepository.findById(parkId)
                .orElseThrow(NotFoundParkException::new);
        parkRepository.deleteById(parkId);

        return ParkResponse.convertToDto(park);
    }

    @Override
    public List<ParkResponse> getFavoritePark(Long memberId) {
        List<FavoriteParkEntity> favoriteParks = favoriteParkRepository.findByMemberMemberId(memberId);

        return favoriteParks.stream()
                .map(favoriteParkEntity -> ParkResponse.convertToDto(favoriteParkEntity.getPark()))
                .collect(Collectors.toList());
    }

    @Override
    public ParkResponse addFavoritePark(Long memberId, Long parkId) {
        ParkEntity park = parkRepository.findById(parkId)
                .orElseThrow(NotFoundParkException::new);
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        FavoriteParkEntity favoritePark = new FavoriteParkEntity(member, park);

        favoriteParkRepository.save(favoritePark);

        return ParkResponse.convertToDto(park);
    }

    @Override
    public ParkResponse deleteFavoritePark(Long memberId, Long parkId) {
        FavoriteParkEntity favoritePark = favoriteParkRepository.findByMemberMemberIdAndParkParkId(memberId, parkId)
                .orElseThrow(NotFoundFavoriteParkException::new);

        favoriteParkRepository.delete(favoritePark);

        return ParkResponse.convertFavoriteToDto(favoritePark);
    }

    private void updateExistingPark(ParkEntity existingPark, ParkRequest parkRequest) {
        if (!parkRequest.getParkName().equals(existingPark.getParkName())) {
            existingPark.setParkName(parkRequest.getParkName());
        }

        if (existingPark.getLongitude() != null) {
            if (!parkRequest.getLongitude().equals(String.valueOf(existingPark.getLongitude()))) {
                existingPark.setLongitude(Double.valueOf(parkRequest.getLongitude()));
            }
        }

        if (existingPark.getLatitude() != null) {
            if (!parkRequest.getLatitude().equals(String.valueOf(existingPark.getLatitude()))) {
                existingPark.setLatitude(Double.valueOf(parkRequest.getLatitude()));
            }
        }

        if (parkRequest.getAddress() != null && !parkRequest.getAddress().isEmpty()) {
            if (!parkRequest.getAddress().equals(existingPark.getAddress())) {
                existingPark.setAddress(parkRequest.getAddress());
            }
        }

        if (parkRequest.getSportFacility() == null) {
            existingPark.setSportFacility("");
        } else if (!parkRequest.getSportFacility().equals(existingPark.getSportFacility())) {
            existingPark.setSportFacility(parkRequest.getSportFacility());
        }


        if (parkRequest.getConvenienceFacility() == null) {
            existingPark.setConvenienceFacility("");
        } else if (!parkRequest.getConvenienceFacility().equals(existingPark.getConvenienceFacility())) {
            existingPark.setConvenienceFacility(parkRequest.getConvenienceFacility());
        }

        parkRepository.save(existingPark);
    }
}
