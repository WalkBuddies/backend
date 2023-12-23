package com.walkbuddies.backend.member.dto;

import com.walkbuddies.backend.member.domain.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    private String email;
    private String password;
    private String checkPassword;
    private String name;
    private String nickname;
    private Integer gender;

    public MemberEntity toEntity(String encodedPassword) {
        LocalDateTime now = LocalDateTime.now();
        return MemberEntity.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .nickname(this.nickname)
                .gender(this.gender)
                .createAt(now)
                .updateAt(now)
                .passwordUpdate(now)
                .build();
    }
}
