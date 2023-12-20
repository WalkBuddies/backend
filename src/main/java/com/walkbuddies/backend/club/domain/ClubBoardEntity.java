package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "club_board")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClubBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clubBoardId;

//    @ManyToOne
//  @JoinColumn(name = "clubId")
    private long clubId;

//    @ManyToOne
//  @JoinColumn(name = "memeberId")
    private long memberId;

    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    private int noticeYn;
    private int deleteYn;
    private int fileYn;

    public static ClubBoardEntity dtoToEntity(ClubBoardDto dto) {

        return ClubBoardEntity.builder().clubBoardId(dto.getClubBoardId()).nickname(dto.getNickname()).title(dto.getTitle()).content(dto.getContent()).createAt(dto.getCreateAt()).updateAt(dto.getUpdateAt()).deleteAt(dto.getDeleteAt()).noticeYn(dto.getNoticeYn()).deleteYn(dto.getDeleteYn()).fileYn(dto.getFileYn()).build();
    }

    public static ClubBoardDto entityToDto(ClubBoardEntity entity) {

        return ClubBoardDto.builder().clubBoardId(entity.getClubBoardId()).nickname(entity.getNickname()).title(entity.getTitle()).content(entity.getContent()).createAt(entity.getCreateAt()).updateAt(entity.getUpdateAt()).deleteAt(entity.getDeleteAt()).noticeYn(entity.getNoticeYn()).deleteYn(entity.getDeleteYn()).fileYn(entity.getFileYn()).build();
    }


}
