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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkServiceImpl implements ParkService {

    private final ParkRepository parkRepository;
    private final FavoriteParkRepository favoriteParkRepository;
    private final MemberRepository memberRepository;

    @Value("${park-service-key}")
    private String serviceKey;

    @Override
    public String buildParkAPIUrl(int pageNo, int numOfRows) {
        String url = "http://api.data.go.kr/openapi/tn_pubr_public_cty_park_info_api" +
                "?serviceKey=" + serviceKey + "&type=json" +
                "&pageNo=" + pageNo + "&numOfRows=" + numOfRows;

        return url;
    }

    @Override
    public String fetchDataFromApi(String apiUrl) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(apiUrl);
        String response = restTemplate.getForObject(uri, String.class);

        return response;
    }

    @Override
    public boolean hasMoreData(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

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
    public List<ParkDto> parseApiResponse(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

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
    public List<ParkDto> getParkList(float longitude, float latitude) {
        List<ParkDto> result = new ArrayList<>();

        List<Object[]> parkList = parkRepository.findNearbyParks(longitude, latitude, 1000);
        for (Object[] parkData : parkList) {
            ParkDto parkDto = new ParkDto();
            parkDto.setParkId((Long) parkData[0]);
            parkDto.setParkName((String) parkData[1]);
            parkDto.setAddress((String) parkData[2]);
            parkDto.setLongitude(String.valueOf(parkData[3]));
            parkDto.setLatitude(String.valueOf(parkData[4]));
            parkDto.setDistance(((Number) parkData[5]).floatValue());

            result.add(parkDto);
        }

        result.sort(Comparator.comparing(ParkDto::getDistance));

        return result;
    }

    @Override
    public Optional<ParkDto> getParkInfo(int parkId) {
        Optional<ParkEntity> parkEntity = parkRepository.findById((long) parkId);

        if (parkEntity.isPresent()) {
            return Optional.of(ParkDto.convertToDto(parkEntity.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void addPark(ParkDto parkDto) {
        ParkEntity parkEntity = ParkEntity.convertToEntity(parkDto);
        parkRepository.save(parkEntity);
    }

    @Override
    @Transactional
    public void updatePark(int parkId, ParkDto newDto) {
        Optional<ParkEntity> optionalPark = parkRepository.findById((long) parkId);

        if (optionalPark.isPresent()) {
            ParkEntity park = optionalPark.get();
            park.setParkName(newDto.getParkName());
            park.setLatitude(Float.parseFloat(newDto.getLatitude()));
            park.setLongitude(Float.parseFloat(newDto.getLongitude()));
            park.setAddress(newDto.getAddress());
            park.setSportFacility(newDto.getSportFacility());
            park.setConvenienceFacility(newDto.getConvenienceFacility());

            parkRepository.save(park);
        } else {
            throw new RuntimeException("Park not found.");
        }
    }

    @Override
    @Transactional
    public void deletePark(int parkId) {
        parkRepository.deleteById((long) parkId);
    }

    @Override
    public List<ParkDto> getFavoritePark(Long memberId) {
        List<FavoriteParkEntity> favoriteParks = favoriteParkRepository.findByMemberMemberId(memberId);

        return favoriteParks.stream()
                .map(favoriteParkEntity -> ParkDto.convertToDto(favoriteParkEntity.getPark()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
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

    @Override
    public boolean isFavoritePark(Long memberId, Long parkId) {
        return favoriteParkRepository.existsByMemberMemberIdAndParkParkId(memberId, parkId);
    }

    private void updateExistingPark(ParkEntity existingPark, ParkDto parkDto) {
        existingPark.setParkName(parkDto.getParkName());

        if (parkDto.getLongitude() != null && !parkDto.getLongitude().isEmpty()) {
            existingPark.setLongitude(Float.parseFloat(parkDto.getLongitude()));
        }
        if (parkDto.getLatitude() != null && !parkDto.getLatitude().isEmpty()) {
            existingPark.setLatitude(Float.parseFloat(parkDto.getLatitude()));
        }
        if (parkDto.getSportFacility() != null && !parkDto.getSportFacility().isEmpty()) {
            existingPark.setSportFacility(parkDto.getSportFacility());
        }
        if (parkDto.getConvenienceFacility() != null && !parkDto.getConvenienceFacility().isEmpty()) {
            existingPark.setConvenienceFacility(parkDto.getConvenienceFacility());
        }

        parkRepository.save(existingPark);
    }
}
