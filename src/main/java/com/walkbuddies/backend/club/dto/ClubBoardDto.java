package com.walkbuddies.backend.club.dto;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClubBoardDto {

  private long clubBoardId;
  private Long clubId;
  private Long memberId;

  private String nickname;
  private String title;
  private String content;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private LocalDateTime deleteAt;
  private int noticeYn;
  private int deleteYn;
  private int fileYn;

}

