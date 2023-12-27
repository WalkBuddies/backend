package com.walkbuddies.backend.club.domain;

import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "club_board_reply")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClubBoardCommentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long clubBoardCommentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private MemberEntity memberId;

  private String nickname;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "clubBoardId", nullable = false)
  private ClubBoardEntity clubBoardId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parentId")
  private ClubBoardCommentEntity parentId;

  @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<ClubBoardCommentEntity> childrenId;

  private String content;
  @CreationTimestamp
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  @ColumnDefault("0")
  private Integer deleteYn;
  private LocalDateTime deleteAt;

  public void updateParent(ClubBoardCommentEntity parentId) {
    this.parentId = parentId;
  }
  public void updateContent(RequestDto dto) {
    this.content = dto.getContent();
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
