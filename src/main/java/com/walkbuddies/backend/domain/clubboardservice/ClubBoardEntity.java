package com.walkbuddies.backend.domain.clubboardservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import com.walkbuddies.backend.dto.clubboardservice.ClubBoardDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.core.Local;

@Entity
@Table(name = "clubBoard")
@Builder
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClubBoardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long clubBoardId;

  @ManyToOne
//  @JoinColumn(name = "clubId")
  private ClubEntity clubId;

  @ManyToOne
//  @JoinColumn(name = "memeberId")
  private MemberEntity memberId;

  private String nickname;
  private String title;
  private String content;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private int noticeYn;
  private int deleteYn;
  private int fileYn;

  public static ClubBoardEntity dtoToEntity(ClubBoardDto dto) {

    return ClubBoardEntity.builder()
        .clubBoardId(dto.getClubBoardId())
        .nickname(dto.getNickname())
        .title(dto.getTitle())
        .content(dto.getContent())
        .createAt(dto.getCreateAt())
        .updateAt(dto.getUpdateAt())
        .noticeYn(dto.getNoticeYn())
        .deleteYn(dto.getDeleteYn())
        .fileYn(dto.getFileYn())
        .build();
  }

  public static ClubBoardDto entityToDto(ClubBoardEntity entity) {

    return ClubBoardDto.builder()
        .clubBoardId(entity.getClubBoardId())
        .nickname(entity.getNickname())
        .title(entity.getTitle())
        .content(entity.getContent())
        .createAt(entity.getCreateAt())
        .updateAt(entity.getUpdateAt())
        .noticeYn(entity.getNoticeYn())
        .deleteYn(entity.getDeleteYn())
        .fileYn(entity.getFileYn())
        .build();
  }



}
