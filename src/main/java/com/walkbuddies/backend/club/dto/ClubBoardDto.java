package com.walkbuddies.backend.club.dto;


import com.walkbuddies.backend.club.domain.ClubBoardEntity;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.common.dto.FileDto;
import com.walkbuddies.backend.member.repository.MemberRepository;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ClubBoardDto {
  private Long clubBoardId;
  private Long clubId;
  private Long memberId;
  private List<FileDto> fileId;

  private String nickname;
  private String title;
  private String content;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private LocalDateTime deleteAt;
  private Integer noticeYn;
  private Integer deleteYn;
  private Integer fileYn;

  }

