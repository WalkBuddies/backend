package com.walkbuddies.backend.feed.domain;

import com.walkbuddies.backend.club.domain.ClubBoardCommentEntity;
import com.walkbuddies.backend.club.dto.clubboardcomment.RequestDto;
import com.walkbuddies.backend.feed.dto.FeedCommentDto;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.nio.MappedByteBuffer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "feedComment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FeedCommentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  Long feedCommentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId", nullable = false)
  private MemberEntity memberId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "feedId", nullable = false)
  private FeedEntity feedId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parentId")
  private FeedCommentEntity parentId;

  @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER, orphanRemoval = true)
  private List<FeedCommentEntity> childrenId;

  private String content;
  @CreationTimestamp
  private LocalDateTime createAt;
  private LocalDateTime updateAt;

  @ColumnDefault("0")
  private Integer deleteYn;
  private LocalDateTime deleteAt;

  public void updateParent(FeedCommentEntity parentId) {
    this.parentId = parentId;
  }
  public void updateContent(FeedCommentDto dto) {
    this.content = dto.getContent();
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
