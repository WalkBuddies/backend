package com.walkbuddies.backend.park.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteParkDto {
    private Long favoriteParkId;
    private Long memberId;
    private Long parkId;
}
