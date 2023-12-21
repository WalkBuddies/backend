package com.walkbuddies.backend.member.dto;

import com.walkbuddies.backend.member.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String email;
    private String name;

    public static MemberDto convertToDto(MemberEntity member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
