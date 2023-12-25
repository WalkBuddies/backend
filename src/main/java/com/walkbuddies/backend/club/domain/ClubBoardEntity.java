package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.ClubBoardDto;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;
import lombok.Builder.Default;
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

    @OneToMany(mappedBy = "fileId", orphanRemoval = true)
    private List<FileEntity> fileId;

    private String nickname;
    private String title;
    private String content;
    @CreationTimestamp
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    @ColumnDefault("0")
    private Integer noticeYn;
    @ColumnDefault("0")
    private Integer deleteYn;
    @ColumnDefault("0")
    private Integer fileYn;

}
