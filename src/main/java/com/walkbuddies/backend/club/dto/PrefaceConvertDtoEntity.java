package com.walkbuddies.backend.club.dto;

import com.walkbuddies.backend.club.domain.ClubPreface;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PrefaceConvertDtoEntity {
  private final ClubPrefaceRepository clubPrefaceRepository;
  private final ClubRepository clubRepository;
  public ClubPrefaceDto prefaceEntityToDto(ClubPreface entity) {
    return ClubPrefaceDto.builder()
        .prefaceId(entity.getPrefaceId())
        .preface(entity.getPreface())
        .clubId(entity.getClubId().getClubId())
        .build();
  }

  public ClubPreface prefaceDtoToEntity(ClubPrefaceDto dto) {
    return ClubPreface.builder()
        .prefaceId(dto.getPrefaceId())
        .preface(dto.getPreface())
        .clubId(clubRepository.findByClubId(dto.getClubId()).get())
        .build();
  }
}
