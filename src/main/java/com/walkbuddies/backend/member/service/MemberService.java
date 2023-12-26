package com.walkbuddies.backend.member.service;

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
}
