package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.clubboard.ClubBoardDto;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clubId")
    private ClubEntity clubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memeberId")
    private MemberEntity memberId;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "clubBoardId")
    private List<FileEntity> fileId;

    private String nickname;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prefaceId")
    private ClubPreface preface;
    @CreationTimestamp
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    private Integer noticeYn;
    private Integer deleteYn;
    private Integer fileYn;

    public void update(ClubBoardDto dto, ClubPreface preface) {

        this.content = dto.getContent();
        this.preface = preface;
        this.title = dto.getTitle();
        this.updateAt = LocalDateTime.now();
    }

    public void changeDeleteYn(int deleteYn) {
        this.deleteYn = deleteYn;
        if (deleteYn == 1) {
            this.deleteAt = LocalDateTime.now();
        } else if (deleteYn == 0) {
            this.deleteAt = null;
        }
    }
}
