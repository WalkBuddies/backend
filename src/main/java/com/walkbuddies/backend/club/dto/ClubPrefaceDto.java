package com.walkbuddies.backend.club.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubPrefaceDto {
  private Long prefaceId;
  private String preface;
  private Long clubId;
}
