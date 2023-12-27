package com.walkbuddies.backend.feed.dto;

import java.time.LocalDateTime;
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
public class FeedCommentDto {
  private Long feedCommentId;
  private Long memberId;
  private Long parentId;
  private Long feedId;
  private String content;
  private String nickname;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private Integer deleteYn;
  private LocalDateTime deleteAt;

}
