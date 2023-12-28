package com.walkbuddies.backend.bookmark.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDto {

    private Long bookmarkId;
    private Long townId;
    private Long memberId;
    private String bookmarkName;
    private LocalDateTime regDate;
}
