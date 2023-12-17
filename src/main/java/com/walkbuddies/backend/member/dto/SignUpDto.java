package com.walkbuddies.backend.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Integer gender;
}
