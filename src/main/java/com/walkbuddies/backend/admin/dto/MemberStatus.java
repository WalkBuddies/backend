package com.walkbuddies.backend.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberStatus {

    private Long memberId;
    private String role;
    private Boolean isBlock;
}
