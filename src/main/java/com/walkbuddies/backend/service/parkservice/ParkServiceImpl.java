package com.walkbuddies.backend.service.parkservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import com.walkbuddies.backend.domain.parkservice.FavoriteParkEntity;
import com.walkbuddies.backend.domain.parkservice.ParkEntity;
import com.walkbuddies.backend.dto.parkservice.ParkDto;
import com.walkbuddies.backend.repository.memberservice.MemberRepository;
import com.walkbuddies.backend.repository.parkservice.FavoriteParkRepository;
import com.walkbuddies.backend.repository.parkservice.ParkRepository;
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

            List<ParkDto> parkDtoList = parseApiResponse(response);
            saveAllParks(parkDtoList);

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
    public List<ParkDto> parseApiResponse(String response) {
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
        List<ParkDto> parkDtoList = new ArrayList<>();
        for (JsonNode parkNode : parkArrayNode) {
            ParkDto parkDto = new ParkDto();

            parkDto.setParkName(parkNode.path("parkNm").asText());

            if (parkNode.has("latitude")) {
                parkDto.setLatitude(parkNode.path("latitude").asText());
            }

            if (parkNode.has("longitude")) {
                parkDto.setLongitude(parkNode.path("longitude").asText());
            }

            parkDto.setAddress(parkNode.path("lnmadr").asText());

            if (parkNode.has("mvmFclty")) {
                parkDto.setSportFacility(parkNode.path("mvmFclty").asText());
            }

            if (parkNode.has("cnvnncFclty")) {
                parkDto.setConvenienceFacility(parkNode.path("cnvnncFclty").asText());
            }

            parkDtoList.add(parkDto);
        }
        return parkDtoList;
    }

    @Override
    @Transactional
    public void saveAllParks(List<ParkDto> parkDtoList) {
        for (ParkDto parkDto : parkDtoList) {
            Optional<ParkEntity> existingPark = parkRepository.findByAddress(parkDto.getAddress());

            if (existingPark.isPresent()) {
                updateExistingPark(existingPark.get(), parkDto);
            } else {
                ParkEntity newPark = ParkEntity.convertToEntity(parkDto);
                parkRepository.save(newPark);
            }
        }
    }

    @Override
    public List<ParkDto> getParkList(Double longitude, Double latitude) {
        List<ParkDto> result = new ArrayList<>();

        List<Object[]> parkList = parkRepository.findNearbyParks(longitude, latitude, 1000);
        for (Object[] park : parkList) {
            ParkDto parkDto = new ParkDto();
            Optional<ParkEntity> optionalPark = parkRepository.findById((Long) park[0]);
            if (optionalPark.isPresent()) {
                ParkEntity parkEntity = optionalPark.get();
                parkDto = ParkDto.convertToDto(parkEntity);
                parkDto.setDistance((Double) park[1]);
            }

            result.add(parkDto);
        }

        result.sort(Comparator.comparing(ParkDto::getDistance));

        return result;
    }

    @Override
    public ParkDto getParkInfo(Long parkId) {
        Optional<ParkEntity> parkEntity = parkRepository.findById(parkId);

        if (parkEntity.isPresent()) {
            return ParkDto.convertToDto(parkEntity.get());
        } else {
            throw new RuntimeException("Park not found.");
        }
    }

    @Override
    public void addPark(ParkDto parkDto) {
        ParkEntity parkEntity = ParkEntity.convertToEntity(parkDto);
        parkRepository.save(parkEntity);
    }

    @Override
    @Transactional
    public void updatePark(Long parkId, ParkDto newDto) {
        Optional<ParkEntity> optionalPark = parkRepository.findById(parkId);

        if (optionalPark.isPresent()) {
            ParkEntity park = optionalPark.get();
            updateExistingPark(park, newDto);

            parkRepository.save(park);
        } else {
            throw new RuntimeException("Park not found.");
        }
    }

    @Override
    public void deletePark(Long parkId) {
        parkRepository.deleteById(parkId);
    }

    @Override
    public List<ParkDto> getFavoritePark(Long memberId) {
        List<FavoriteParkEntity> favoriteParks = favoriteParkRepository.findByMemberMemberId(memberId);

        return favoriteParks.stream()
                .map(favoriteParkEntity -> ParkDto.convertToDto(favoriteParkEntity.getPark()))
                .collect(Collectors.toList());
    }

    @Override
    public void addFavoritePark(Long memberId, Long parkId) {
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);
        Optional<ParkEntity> optionalPark = parkRepository.findById(parkId);

        if (optionalMember.isPresent() && optionalPark.isPresent()) {
            MemberEntity member = optionalMember.get();
            ParkEntity park = optionalPark.get();

            FavoriteParkEntity favoritePark = new FavoriteParkEntity(member, park);

            favoriteParkRepository.save(favoritePark);
        } else {
            throw new RuntimeException("Member or Park not found.");
        }
    }

    @Override
    public void deleteFavoritePark(Long memberId, Long parkId) {
        Optional<FavoriteParkEntity> optionalFavoritePark = favoriteParkRepository.findByMemberMemberIdAndParkParkId(memberId, parkId);

        if (optionalFavoritePark.isPresent()) {
            favoriteParkRepository.delete(optionalFavoritePark.get());
        } else {
            throw new RuntimeException("Favorite Park not found.");
        }
    }

    private void updateExistingPark(ParkEntity existingPark, ParkDto parkDto) {
        if (!parkDto.getParkName().equals(existingPark.getParkName())) {
            existingPark.setParkName(parkDto.getParkName());
        }

        if (existingPark.getLongitude() != null) {
            if (!parkDto.getLongitude().equals(String.valueOf(existingPark.getLongitude()))) {
                existingPark.setLongitude(Double.valueOf(parkDto.getLongitude()));
            }
        }

        if (existingPark.getLatitude() != null) {
            if (!parkDto.getLatitude().equals(String.valueOf(existingPark.getLatitude()))) {
                existingPark.setLatitude(Double.valueOf(parkDto.getLatitude()));
            }
        }

        if (parkDto.getAddress() != null && !parkDto.getAddress().isEmpty()) {
            if (!parkDto.getAddress().equals(existingPark.getAddress())) {
                existingPark.setAddress(parkDto.getAddress());
            }
        }

        if (parkDto.getSportFacility() == null) {
            existingPark.setSportFacility("");
        } else if (!parkDto.getSportFacility().equals(existingPark.getSportFacility())) {
                existingPark.setSportFacility(parkDto.getSportFacility());
        }


        if (parkDto.getConvenienceFacility() == null) {
            existingPark.setConvenienceFacility("");
        } else if (!parkDto.getConvenienceFacility().equals(existingPark.getConvenienceFacility())) {
            existingPark.setConvenienceFacility(parkDto.getConvenienceFacility());
        }

        parkRepository.save(existingPark);
    }
}
