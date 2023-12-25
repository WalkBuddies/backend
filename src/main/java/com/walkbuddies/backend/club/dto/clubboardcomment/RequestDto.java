package com.walkbuddies.backend.club.dto.clubboardcomment;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.repository.ClubBoardCommentRepository;
import com.walkbuddies.backend.club.repository.ClubBoardRepository;
import com.walkbuddies.backend.member.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
public class RequestDto {
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
