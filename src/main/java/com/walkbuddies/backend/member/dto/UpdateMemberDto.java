package com.walkbuddies.backend.member.dto;

import com.walkbuddies.backend.member.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMemberDto {

    // 한별님이 날린 PR에 UpdateMemberDto가 포함이 안되어 있어 제가 일단 대충 만들어 놓은 파일입니다.
    // 차후에 한별님이 완성해주세요.

    private Long memberId;

    public static UpdateMemberDto fromEntity(MemberEntity member) {

        return null;
    }

    public String getNickname() {
        return null;
    }

    public String getIntroduction() {
        return null;
    }

    public String getImageUrl() {
        return null;
    }
}
