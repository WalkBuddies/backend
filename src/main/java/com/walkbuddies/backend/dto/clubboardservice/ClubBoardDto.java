package com.walkbuddies.backend.dto.clubboardservice;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClubBoardDto {

  private long clubBoardId;
  private Long clubId;
  private Long memberId;

  private String nickname;
  private String title;
  private String content;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private int noticeYn;
  private int deleteYn;
  private int fileYn;

}
