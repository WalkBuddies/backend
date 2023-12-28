package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.member.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    MemberResponse signUp(SignUpRequest member);

    MemberResponse verify(String email, String code);

    MemberResponse login(LoginRequest loginRequest);

    void logout(String accessToken, String email);

    MemberResponse resetPassword(ResetPasswordRequest request);

    String getNameById(Long memberId);

    Long getDong(Double longitude, Double latitude);

    MemberTownResponse addTown(Long memberId, Long townId);
}
