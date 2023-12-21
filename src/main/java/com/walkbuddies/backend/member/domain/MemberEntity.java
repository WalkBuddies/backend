package com.walkbuddies.backend.member.domain;

import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.common.SHA256;
import com.walkbuddies.backend.member.dto.SignUpDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private String salt;

    private LocalDateTime passwordUpdate;

    private boolean verify;

    private String verificationCode;

    private LocalDateTime verifyExpiredAt;

    private Integer loginType;

    private String imageUrl;

    private String introduction;

    private Integer socialCode;

    private String oauthExternalId;

    private String accessToken;

    private LocalDateTime townVerificationDate;

    public MemberEntity(SignUpDto signUpDto) {
        this.email = signUpDto.getEmail();
        this.salt = SHA256.createSalt();
        this.password = SHA256.getEncrypt(signUpDto.getPassword(), salt);
        this.name = signUpDto.getName();
        this.nickname = signUpDto.getNickname();
        this.gender = signUpDto.getGender();
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.passwordUpdate = LocalDateTime.now();
    }

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
}