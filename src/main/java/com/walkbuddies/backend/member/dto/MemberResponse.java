package com.walkbuddies.backend.member.dto;

import com.walkbuddies.backend.member.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberResponse {
    private String email;
    private String name;
    private String nickname;
    private String role;

    private MemberResponse(MemberEntity member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.role = member.getRole();
        this.name = member.getName();
    }

    public static MemberResponse fromEntity(MemberEntity member) {
        return new MemberResponse(member);
    }
}
