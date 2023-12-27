package com.walkbuddies.backend.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    private Long memberId;
    private Long townId;
    private String email;
    private String role;
    private boolean blocked;
    private String password;
    private String name;
    private String nickName;
    private Integer gender;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String imageUrl;
    private String introduction;
    private LocalDateTime passwordUpdate;
    private Integer socialCode;
    private String oauthExternalId;
    private boolean verify;
    private String verificationCode;
    private LocalDateTime verifyExpiredAt;
    private LocalDateTime townVerificationDate;

}
