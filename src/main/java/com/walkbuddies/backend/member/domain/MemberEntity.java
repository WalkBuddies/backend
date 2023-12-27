package com.walkbuddies.backend.member.domain;

import com.walkbuddies.backend.admin.dto.MemberDetailDto;
import com.walkbuddies.backend.club.domain.TownEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "townId")
    private TownEntity townId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;    // USER, ADMIN

    @Column(nullable = false)
    private boolean blocked;

    @Column(nullable = false)
    private String password;
    private LocalDateTime passwordUpdate;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String nickname;

    private Integer gender;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private boolean verify;
    private String verificationCode;
    private LocalDateTime verifyExpiredAt;

    private String imageUrl;
    private String introduction;

    private Integer socialCode;
    private String oauthExternalId;

    private LocalDateTime townVerificationDate;

    public void createVerificationRequest(String code) {
        this.verify = false;
        this.verificationCode = code;
        this.verifyExpiredAt = LocalDateTime.now().plusDays(1);
    }

    public void verify() {
        this.verify = true;
        this.verificationCode = null;
        this.verifyExpiredAt = null;
    }

    public void createPasswordRequest(String code, String tempPassword) {
        this.verify = false;
        this.verificationCode = code;
        this.password = tempPassword;
        this.passwordUpdate = LocalDateTime.now();
        this.verifyExpiredAt = LocalDateTime.now().plusDays(1);
    }

    public void updateMember(String nickName, String introduction, String imageUrl) {
        this.nickname = nickName;
        this.introduction = introduction;
        this.imageUrl = imageUrl;
        this.updateAt = LocalDateTime.now();
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
                .imageUrl(memberEntity.getImageUrl())
                .introduction(memberEntity.getIntroduction())
                .passwordUpdate(memberEntity.getPasswordUpdate())
                .socialCode(memberEntity.getSocialCode())
                .oauthExternalId(memberEntity.getOauthExternalId())
                .verify(memberEntity.isVerify())
                .verificationCode(memberEntity.getVerificationCode())
                .verifyExpiredAt(memberEntity.getVerifyExpiredAt())
                .townVerificationDate(memberEntity.getTownVerificationDate())
                .build();
    }
}