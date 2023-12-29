package com.walkbuddies.backend.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
  private Long fileId;
  private Long clubId;
  private Long feedId;
  private String originalName;
  private String savedName;
  private Long size;
  private LocalDateTime createAt;


}
