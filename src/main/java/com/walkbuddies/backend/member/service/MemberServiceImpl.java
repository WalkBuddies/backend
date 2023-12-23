package com.walkbuddies.backend.member.service;

import com.walkbuddies.backend.common.MailService;
import com.walkbuddies.backend.exception.impl.*;
import com.walkbuddies.backend.common.JwtTokenUtil;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.dto.MemberResponse;
import com.walkbuddies.backend.member.dto.SignUpRequest;
import com.walkbuddies.backend.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder encoder;
    private final HttpSession httpSession;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private final long EXPIRE_TIME_MS = 1000 * 60 * 60 * 24 * 7;

    @Override
    @Transactional
    public MemberResponse signUp(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);

        String encodedPassword = encoder.encode(signUpRequest.getPassword());
        MemberEntity member = signUpRequest.toEntity(encodedPassword);
        member.createVerificationRequest(generateVerificationCode());
        sendVerificationEmail(member.getEmail(), member.getVerificationCode());
        memberRepository.save(member);

        return MemberResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberResponse verify(String email, String code) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);

        if (member.isVerify()) {
            throw new AlreadyVerifiedMemberException();
        }

        if (member.getVerificationCode() != null && member.getVerificationCode().equals(code)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationTime = member.getVerifyExpiredAt();
            if (expirationTime != null && now.isBefore(expirationTime)) {
                member.verify();
                memberRepository.save(member);
            } else {
                throw new ExpiredCodeException();
            }
        } else {
            throw new CodeMismatchException();
        }

        return MemberResponse.fromEntity(member);
    }

    @Override
    public String login(String email, String password) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);

        if (!encoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException();
        }

        return JwtTokenUtil.createToken(email, EXPIRE_TIME_MS, secretKey);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();

        httpSession.invalidate();
    }

    private void sendVerificationEmail(String toEmail, String verificationCode) {
        if (!isValidEmail(toEmail)) {
            throw new InvalidEmailException();
        }
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String title = "WalkBuddies 회원가입 인증";
        String url = baseUrl + "/member/verify?email=" + toEmail + "&code=" + verificationCode;
        String message = "<h3>WalkBuddies 회원가입을 완료하려면 아래 링크를 클릭하세요.</h3>" +
                "<a href='" + url + "' target='_blank'>인증 링크</a>";

        mailService.sendEmail(toEmail, title, message);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void checkDuplicatedEmail(String email) {
        Optional<MemberEntity> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new DuplicatedEmailException();
        }
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        // 이메일 중복 확인
        this.checkDuplicatedEmail(signUpRequest.getEmail());

        // 닉네임 중복 확인
        if (memberRepository.existsByNickname(signUpRequest.getNickname())) {
            throw new DuplicatedNicknameException();
        }

        // 패스워드 검증
        this.checkValidPassword(signUpRequest.getPassword());

        // 필수 필드 확인
        if (signUpRequest.getName() == null || signUpRequest.getName().isEmpty()
                || signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()
                || signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()
                || signUpRequest.getNickname() == null || signUpRequest.getNickname().isEmpty()) {
            throw new MissingFieldException();
        }

        // 닉네임 길이 확인
        if (signUpRequest.getNickname().length() < 3 || signUpRequest.getNickname().length() > 20) {
            throw new NicknameLengthException();
        }

        // 패스워드 확인
        if (!signUpRequest.getPassword().equals(signUpRequest.getCheckPassword())) {
            throw new PasswordMismatchException();
        }
    }

    private void checkValidPassword(String password) {
        final int MIN = 8;
        final int MAX = 16;

        final String REGEX = "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
        final String SAMEPT = "(\\w)\\1\\1\\1";

        Matcher matcher;

        // 패스워드 포맷(영문, 특수문자, 숫자 포함 길이 제한)
        matcher = Pattern.compile(REGEX).matcher(password);
        if (!matcher.find()) {
            throw new PasswordException();
        }

        // 동일문자
        matcher = Pattern.compile(SAMEPT).matcher(password);
        if (matcher.find()) {
            throw new PasswordException();
        }
    }

    private String generateVerificationCode() {
        int length = 6;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
