package com.walkbuddies.backend.bookmark.dto;

import com.walkbuddies.backend.bookmark.domain.BookmarkEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDto {

    private long bookmarkId;
    private long townId;
    private long memberId;
    private String bookmarkName;
    private LocalDateTime regDate;
}
