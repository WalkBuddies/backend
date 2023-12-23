package com.walkbuddies.backend.member.dto;

import com.walkbuddies.backend.member.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private String email;
    private String name;

    public static MemberResponse fromEntity(MemberEntity member) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
