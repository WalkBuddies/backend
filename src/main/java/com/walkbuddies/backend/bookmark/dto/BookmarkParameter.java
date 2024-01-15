package com.walkbuddies.backend.bookmark.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkParameter {

    private String townName;
    private long memberId;
    private String bookmarkName;
    private long bookmarkId;
}
