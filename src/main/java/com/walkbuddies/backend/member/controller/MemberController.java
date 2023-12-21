package com.walkbuddies.backend.member.controller;

import com.walkbuddies.backend.common.response.SingleResponse;
import com.walkbuddies.backend.member.dto.SignUpDto;
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
    public ResponseEntity<SingleResponse> signUp(@RequestBody SignUpDto signUpDto) {
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "인증 메일이 발송되었습니다.",
                memberService.signUp(signUpDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<SingleResponse> verifyEmail(@RequestParam String email, @RequestParam String code) {
        SingleResponse response = new SingleResponse(HttpStatus.OK.value(),
                "이메일 주소가 인증되었습니다.",
                memberService.verify(email, code));
        return ResponseEntity.ok(response);
    }
}
