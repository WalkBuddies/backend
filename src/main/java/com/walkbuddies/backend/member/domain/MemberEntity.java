package com.walkbuddies.backend.member.domain;

import com.walkbuddies.backend.admin.dto.MemberDetailDto;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.common.SHA256;
import com.walkbuddies.backend.member.dto.SignUpDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "townId")
    private TownEntity townId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String nickname;

    private Integer gender;

    @Column
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Column
    @Temporal(TemporalType.DATE)
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

    public MemberEntity(SignUpDto dto) {
        String salt = SHA256.createSalt();
        this.email = dto.getEmail();
        this.password = SHA256.getEncrypt(dto.getPassword(), salt);
        this.name = dto.getName();
        this.nickname = dto.getNickname();
        this.gender = dto.getGender();
        this.createAt = new Date();
        this.updateAt = new Date();
        this.salt = salt;
        this.passwordUpdate = new Date();
    }

    public static MemberDetailDto entityToDetail(MemberEntity memberEntity) {

        return MemberDetailDto.builder()
                .memberId(memberEntity.getMemberId())
                .townId(memberEntity.getTownId().getTownId())
                .email(memberEntity.getEmail())
                .password(memberEntity.getPassword())
                .name(memberEntity.getName())
                .nickName(memberEntity.getNickname())
                .gender(memberEntity.getGender())
                .createAt(memberEntity.getCreateAt())
                .updateAt(memberEntity.getUpdateAt())
                .loginType(memberEntity.getLoginType())
                .imageUrl(memberEntity.getImageUrl())
                .introduction(memberEntity.getIntroduction())
                .salt(memberEntity.getSalt())
                .passwordUpdate(memberEntity.getPasswordUpdate())
                .socialCode(memberEntity.getSocialCode())
                .oauthExternalId(memberEntity.getOauthExternalId())
                .accessToken(memberEntity.getAccessToken())
                .verify(memberEntity.isVerify())
                .verificationCode(memberEntity.getVerificationCode())
                .verifyExpiredAt(memberEntity.getVerifyExpiredAt())
                .townVerificationDate(memberEntity.getTownVerificationDate())
                .build();
    }
}