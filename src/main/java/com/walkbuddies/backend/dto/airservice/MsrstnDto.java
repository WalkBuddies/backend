package com.walkbuddies.backend.dto.airservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MsrstnDto {
  private int stationCode;
  private String addr;
  private String stationName;
}
