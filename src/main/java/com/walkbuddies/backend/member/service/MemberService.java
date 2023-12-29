package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.member.dto.*;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.dto.*;
import com.walkbuddies.backend.member.dto.LoginRequest;
import com.walkbuddies.backend.member.dto.MemberResponse;
import com.walkbuddies.backend.member.dto.ResetPasswordRequest;
import com.walkbuddies.backend.member.dto.SignUpRequest;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberResponse signUp(SignUpRequest member);

    MemberResponse verify(String email, String code);

    MemberResponse login(LoginRequest loginRequest);

    void logout(String accessToken, String email);

    MemberResponse resetPassword(ResetPasswordRequest request);

    String getNameById(Long memberId);

    void update(MemberEntity member, UpdateMemberDto updateMemberDto);

    MemberEntity getMemberEntity(Long memberId);

    Long getDong(Double longitude, Double latitude);

    MemberTownResponse addTown(Long memberId, Long townId);

    MemberEntity getMemberEntity(Long memberId);
}
