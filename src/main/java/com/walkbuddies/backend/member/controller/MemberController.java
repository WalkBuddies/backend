package com.walkbuddies.backend.member.controller;

import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.member.dto.*;
import com.walkbuddies.backend.member.jwt.JwtTokenUtil;
import com.walkbuddies.backend.member.security.MemberDetails;
import com.walkbuddies.backend.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup")
    public ResponseEntity<SingleResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "인증 메일이 발송되었습니다.",
                memberService.signUp(signUpRequest));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<SingleResponse> verifyEmail(@RequestParam String email, @RequestParam String code) {
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "이메일 주소가 인증되었습니다.",
                memberService.verify(email, code));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<SingleResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        MemberResponse memberResponse = memberService.login(loginRequest);
        TokenResponse tokenResponse = jwtTokenUtil.createTokenByLogin(memberResponse.getEmail(), "USER");
        response.addHeader(JwtTokenUtil.AUTHORIZATION_HEADER, tokenResponse.getAccessToken());
        SingleResponse singleResponse = new SingleResponse(HttpStatus.OK.value(),
                "로그인 되었습니다.",
                tokenResponse);
        return ResponseEntity.ok(singleResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<SingleResponse> logout(
            @AuthenticationPrincipal MemberDetails memberDetails,
            HttpServletRequest request) {

        if (memberDetails != null) {
            String accessToken = jwtTokenUtil.resolveToken(request);
            memberService.logout(accessToken, memberDetails.getUsername());
            SingleResponse response = new SingleResponse<>(HttpStatus.OK.value(),
                    "로그아웃 되었습니다.",
                    null);
            return ResponseEntity.ok(response);
        } else {
            SingleResponse response = new SingleResponse<>(HttpStatus.UNAUTHORIZED.value(), "로그인 상태가 아닙니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SingleResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "인증 메일이 발송되었습니다.",
                memberService.resetPassword(request));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue-token")
    public ResponseEntity<SingleResponse> reissueToken(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody ReissueTokenRequest tokenRequest) {

        MemberResponse memberResponse = MemberResponse.fromEntity(memberDetails.getMember());

        TokenResponse tokenResponse = jwtTokenUtil.reissueToken(memberResponse.getEmail(), "USER", tokenRequest.getRefreshToken());
        return ResponseEntity.ok(new SingleResponse<>(HttpStatus.OK.value(), "토큰 재발행", tokenResponse));
    }

    @PostMapping("/town")
    public ResponseEntity<SingleResponse> addTown(
            @AuthenticationPrincipal MemberDetails memberDetails,
//            @PathVariable Long memberId,
            @RequestParam("longitude") Double longitude,
            @RequestParam("latitude") Double latitude) {
        Long memberId = memberDetails.getMember().getMemberId();

        SingleResponse response = new SingleResponse<>(HttpStatus.OK.value(),
                "동네 인증되었습니다.",
                memberService.addTown(memberId, memberService.getDong(longitude, latitude)));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getdong")
    public Long getLegalDong(
            @RequestParam("longitude") Double longitude,
            @RequestParam("latitude") Double latitude
    ) {
        return memberService.getDong(longitude, latitude);
    }
}
