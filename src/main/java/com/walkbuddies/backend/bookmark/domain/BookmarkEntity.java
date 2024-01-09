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
    private TownEntity town;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    private String bookmarkName;
    private LocalDateTime regDate;

}
