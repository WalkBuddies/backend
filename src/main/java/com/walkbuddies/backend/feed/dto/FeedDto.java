package com.walkbuddies.backend.feed.dto;

import com.walkbuddies.backend.common.dto.FileDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class FeedDto {
  private Long feedId;
  private Long memberId;
  private List<FileDto> fileId;
  private String title;
  private String content;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private LocalDateTime deleteAt;
  private Integer deleteYn;
  private Integer fileYn;

}
