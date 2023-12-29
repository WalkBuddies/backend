package com.walkbuddies.backend.bookmark.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkParameter {

    private String townName;
    private Long memberId;
    private String bookmarkName;
}
