package com.walkbuddies.backend.domain.memberservice;

import com.walkbuddies.backend.domain.clubservice.TownEntity;
import com.walkbuddies.backend.dto.memberservice.SignUpDto;
import com.walkbuddies.backend.util.SHA256;
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
}