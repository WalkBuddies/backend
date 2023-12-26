package com.walkbuddies.backend.admin.dto;

import com.walkbuddies.backend.club.domain.TownEntity;
import lombok.*;

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
    private Date createAt;
    private Date updateAt;
    private Integer loginType;
    private String imageUrl;
    private String introduction;
    private String salt;
    private Date passwordUpdate;
    private Integer socialCode;
    private String oauthExternalId;
    private String accessToken;
    private boolean verify;
    private String verificationCode;
    private Date verifyExpiredAt;
    private Date townVerificationDate;

}
