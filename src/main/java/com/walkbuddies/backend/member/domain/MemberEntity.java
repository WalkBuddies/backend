package com.walkbuddies.backend.member.domain;

import com.walkbuddies.backend.club.domain.TownEntity;
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
        this.verifyExpiredAt = LocalDateTime.now().plusDays(1);
    }
}