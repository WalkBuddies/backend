package com.walkbuddies.backend.member.controller;

import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.member.dto.LoginRequest;
import com.walkbuddies.backend.member.dto.SignUpRequest;
import com.walkbuddies.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
    public ResponseEntity<SingleResponse> login(@RequestBody LoginRequest loginRequest){
        String token = memberService.login(loginRequest.getEmail(), loginRequest.getPassword());
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(), "로그인 성공", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<SingleResponse> logout() {
        memberService.logout();
        SingleResponse response = new SingleResponse<>(HttpStatus.OK.value(), "로그아웃 되었습니다.", null);
        return ResponseEntity.ok(response);
    }
}
