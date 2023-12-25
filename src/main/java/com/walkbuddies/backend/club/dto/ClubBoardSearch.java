package com.walkbuddies.backend.club.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@Builder
public class ClubBoardSearch {

  private String keyword;
  private String type;

}
