package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    private Long clubBoardId;

    @ManyToOne
    @JoinColumn(name = "clubId")
    private ClubEntity clubId;

    @ManyToOne
    @JoinColumn(name = "memeberId")
    private MemberEntity memberId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fileId")
    private List<FileEntity> fileId;

    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    private Integer noticeYn;
    private Integer deleteYn;
    private Integer fileYn;

    public ClubBoardEntity dtoToEntity(ClubBoardDto dto) {
        final FileEntity fileEntity = new FileEntity();
        return ClubBoardEntity.builder()
            .clubBoardId(dto.getClubBoardId())
            .nickname(dto.getNickname())
            .fileId(fileEntity.dtoListToEntityList(dto.getFileId()))
            .title(dto.getTitle())
            .content(dto.getContent())
            .createAt(dto.getCreateAt())
            .updateAt(dto.getUpdateAt())
            .deleteAt(dto.getDeleteAt())
            .noticeYn(dto.getNoticeYn())
            .deleteYn(dto.getDeleteYn())
            .fileYn(dto.getFileYn())
            .build();
    }

    public ClubBoardDto entityToDto(ClubBoardEntity entity) {
        final FileEntity fileEntity = new FileEntity();
        return ClubBoardDto.builder()
            .clubBoardId(entity.getClubBoardId())
            .fileId(fileEntity.entityListToDtoList(entity.getFileId()))
            .clubId(entity.getClubId().getClubId())
            .memberId(entity.getMemberId().getMemberId())
            .nickname(entity.getNickname())
            .title(entity.getTitle())
            .content(entity.getContent())
            .createAt(entity.getCreateAt())
            .updateAt(entity.getUpdateAt())
            .deleteAt(entity.getDeleteAt())
            .noticeYn(entity.getNoticeYn())
            .deleteYn(entity.getDeleteYn())
            .fileYn(entity.getFileYn())
            .build();
    }



}
