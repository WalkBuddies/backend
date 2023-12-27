package com.walkbuddies.backend.admin.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    private Long memberId;
    private Long townId;
    private String email;
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
    private String accessToken;
    private boolean verify;
    private String verificationCode;
    private LocalDateTime verifyExpiredAt;
    private LocalDateTime townVerificationDate;

}
