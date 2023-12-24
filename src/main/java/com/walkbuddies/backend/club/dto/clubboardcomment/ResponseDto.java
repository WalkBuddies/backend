package com.walkbuddies.backend.club.dto.clubboardcomment;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ResponseDto {

  private Long clubBoardCommentId;
  private Long memberId;
  private Long parentId;
  private String nickname;
  private Long clubBoardId;
  private String content;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private Integer deleteYn;
  private LocalDateTime deleteAt;


  }
