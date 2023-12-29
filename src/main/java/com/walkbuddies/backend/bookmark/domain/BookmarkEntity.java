package com.walkbuddies.backend.bookmark.domain;

import com.walkbuddies.backend.bookmark.dto.BookmarkDto;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.member.domain.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmark")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private TownEntity townId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberId;

    private String bookmarkName;
    private LocalDateTime regDate;

    public static BookmarkDto entityToDto(BookmarkEntity bookmarkEntity) {

        return BookmarkDto.builder()
                .bookmarkId(bookmarkEntity.getBookmarkId())
                .townId(bookmarkEntity.getTownId().getTownId())
                .memberId(bookmarkEntity.getMemberId().getMemberId())
                .bookmarkName(bookmarkEntity.getBookmarkName())
                .regDate(bookmarkEntity.getRegDate())
                .build();
    }
}
