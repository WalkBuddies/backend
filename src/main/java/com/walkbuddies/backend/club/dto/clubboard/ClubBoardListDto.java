package com.walkbuddies.backend.club.dto.clubboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClubBoardListDto {

  private long clubId;
  private String keyword;
  private String type;

}
