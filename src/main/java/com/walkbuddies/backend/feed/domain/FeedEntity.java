package com.walkbuddies.backend.feed.domain;

import com.walkbuddies.backend.common.domain.FileEntity;
import com.walkbuddies.backend.feed.dto.FeedDto;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "feed")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long feedId;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private MemberEntity memberId;
  @OneToMany(orphanRemoval = true)
  @JoinColumn(name = "feedId")
  private List<FileEntity> fileId;
  private String title;
  private String content;
  @CreationTimestamp
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private LocalDateTime deleteAt;
  @ColumnDefault("0")
  private Integer deleteYn;
  @ColumnDefault("0")
  private Integer fileYn;

  public void update (FeedDto dto) {
    this.content = dto.getContent();
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
