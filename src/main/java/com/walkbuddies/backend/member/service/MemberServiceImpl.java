package com.walkbuddies.backend.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.repository.TownRepository;
import com.walkbuddies.backend.exception.impl.*;
import com.walkbuddies.backend.member.cache.CacheNames;
import com.walkbuddies.backend.member.dto.*;
import com.walkbuddies.backend.member.email.EmailConfig;
import com.walkbuddies.backend.member.jwt.JwtTokenUtil;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
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
    private final TownRepository townRepository;
    private final EmailConfig.MailService mailService;
    private final RedisService redisService;
    private final BCryptPasswordEncoder encoder;

    @Value("${naver-api-key-id}")
    private String naverApiKeyId;

    @Value("${naver-api-key-secret}")
    private String naverApiKeySecret;

    @Override
    @Transactional
    public MemberResponse signUp(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);

        String encodedPassword = encoder.encode(signUpRequest.getPassword());
        MemberEntity member = signUpRequest.toEntity("USER", encodedPassword);
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

    @Cacheable(cacheNames = CacheNames.LOGINUSER, key = "'login' + #p0.getEmail()", unless = "#result==null")
    @Transactional
    @Override
    public MemberResponse login(LoginRequest request) {
        MemberEntity member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(NotFoundMemberException::new);

        if (!encoder.matches(request.getPassword(), member.getPassword())) {
            throw new PasswordMismatchException();
        }

       if (!member.isVerify()) {
           throw new NotVerifiedException();
       }

        return MemberResponse.fromEntity(member);
    }

    @CacheEvict(cacheNames = CacheNames.USERBYEMAIL, key = "'login'+#p1")
    @Transactional
    @Override
    public void logout(String accessToken, String email) {
        Long expiration = JwtTokenUtil.getExpiration(accessToken);
        redisService.setBlackList(accessToken, "logout", expiration);
        if (redisService.hasKey(email)) {
            redisService.deleteRefreshToken(email);
        } else {
            throw new AlreadyLogoutException();
        }
    }

    @Override
    @Transactional
    public MemberResponse resetPassword(ResetPasswordRequest request) {
        MemberEntity member = memberRepository.findByEmailAndName(request.getEmail(), request.getName())
                .orElseThrow(NotFoundMemberException::new);

        String tempPassword = generateTempPassword();
        String code = generateVerificationCode();
        member.createPasswordRequest(code, encoder.encode(tempPassword));
        memberRepository.save(member);

        sendPasswordEmail(member.getEmail(), code, tempPassword);

        return MemberResponse.fromEntity(member);
    }

    @Override
    public String getNameById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        return member.getName();
    }

    @Override
    public Long getDong(Double longitude, Double latitude) {
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc" +
                "?request=coordsToaddr&coords=" + latitude + "," + longitude +
                "&sourcecrs=epsg:4326&output=json&orders=legalcode";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", naverApiKeyId);
        headers.set("X-NCP-APIGW-API-KEY", naverApiKeySecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        Long codeId = Long.valueOf(parseDongCodeFromJson(responseEntity.getBody()));
        return codeId;
    }

    private String parseDongCodeFromJson(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = jsonNode.path("results").path(0);
            JsonNode codeNode = resultsNode.path("code").path("id");

            return codeNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public MemberTownResponse addTown(Long memberId, Long townId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        TownEntity town = townRepository.findById(townId)
                .orElseThrow(NotFoundTownException::new);

        member.setTown(town);

        return new MemberTownResponse(member);
    }

    public MemberEntity getMemberEntity(Long memberId) {
        Optional<MemberEntity> op = memberRepository.findByMemberId(memberId);
        if (op.isEmpty()) {
            throw new NotFoundMemberException();
        }

        return op.get();
    }
    public String getNameById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
        return member.getName();
    }

    private String generateTempPassword() {
        final int MIN = 8;
        final int MAX = 16;

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder tempPassword = new StringBuilder();

        Random random = new Random();

        // 임시 비밀번호 길이를 랜덤하게 설정 (최소 MIN, 최대 MAX)
        int passwordLength = random.nextInt(MAX - MIN + 1) + MIN;

        // 임시 비밀번호 생성 규칙에 따라 문자를 랜덤하게 선택
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            tempPassword.append(characters.charAt(index));
        }

        // 생성된 임시 비밀번호가 규칙에 맞지 않으면 재귀적으로 다시 생성
        if (!isValidPassword(tempPassword.toString())) {
            return generateTempPassword();
        }

        return tempPassword.toString();
    }

    private void sendPasswordEmail(String email, String code, String tempPassword) {
        if (!isValidEmail(email)) {
            throw new InvalidEmailException();
        }
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String title = "WalkBuddies 임시 비밀번호";
        String url = baseUrl + "/member/verify?email=" + email + "&code=" + code;
        String message = "<h3>인증을 위해 아래 링크를 클릭하세요.</h3>" +
                "<a href='" + url + "' target='_blank'>임시 비밀번호 : " + tempPassword + "</a>";

        mailService.sendEmail(email, title, message);

    }

    private void sendVerificationEmail(String toEmail, String verificationCode) {
        if (!isValidEmail(toEmail)) {
            throw new InvalidEmailException();
        }
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String title = "WalkBuddies 회원 인증";
        String url = baseUrl + "/member/verify?email=" + toEmail + "&code=" + verificationCode;
        String message = "<h3>WalkBuddies 회원 인증을 완료하려면 아래 링크를 클릭하세요.</h3>" +
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
        if (!isValidPassword(signUpRequest.getPassword())) {
            throw new PasswordException();
        }

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

    private boolean isValidPassword(String password) {
        final int MIN = 8;
        final int MAX = 16;

        final String REGEX = "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + MIN + "," + MAX + "})$";
        final String SAMEPT = "(\\w)\\1\\1\\1";

        Matcher matcher;

        // 패스워드 포맷(영문, 특수문자, 숫자 포함 길이 제한)
        matcher = Pattern.compile(REGEX).matcher(password);
        if (!matcher.find()) {
            return false;
        }

        // 동일문자
        matcher = Pattern.compile(SAMEPT).matcher(password);
        if (matcher.find()) {
            return false;
        }

        return true;
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

    @Override
    @Transactional
    public void update(MemberEntity existingMember, UpdateMemberDto updateMemberDto) {
         existingMember.updateMember(updateMemberDto.getNickname(),
                updateMemberDto.getIntroduction(),
                updateMemberDto.getImageUrl());

        memberRepository.save(existingMember);
    }
}
