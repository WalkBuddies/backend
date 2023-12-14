package com.walkbuddies.backend.service.clubservice;

import com.walkbuddies.backend.domain.clubservice.ClubEntity;
import com.walkbuddies.backend.domain.clubservice.MyClubEntity;
import com.walkbuddies.backend.domain.memberservice.MemberEntity;
import com.walkbuddies.backend.dto.clubservice.ClubDto;
import com.walkbuddies.backend.dto.clubservice.ClubJoinInform;
import com.walkbuddies.backend.dto.clubservice.ClubResponse;
import com.walkbuddies.backend.repository.clubservice.ClubRepository;
import com.walkbuddies.backend.repository.clubservice.MyClubRepository;
import com.walkbuddies.backend.repository.memberservice.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MyClubRepository myClubRepository;
    private final MemberRepository memberRepository;

    /**
     * 소모임을 생성하는 메서드.
     * @param clubDto
     * @return
     */
    @Transactional
    @Override
    public ClubResponse createClub(ClubDto clubDto) {

        Optional<ClubEntity> existingClub = clubRepository.findByClubName(clubDto.getClubName());
        if (existingClub.isPresent()) {
            throw new EntityExistsException("이미 존재하는 소모임 이름 입니다: "  + clubDto.getClubName());
        }

        MemberEntity member = memberRepository.findById(clubDto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다: " + clubDto.getOwnerId()));

       if (!clubDto.getTownId().equals(member.getTownId())) {
           throw new RuntimeException("내 동네가 아닙니다.");
       }

        ClubEntity clubEntity = ClubEntity.builder()
                .clubName(clubDto.getClubName())
                .townId(member)
                .ownerId(member)
                .members(1)
                .membersLimit(clubDto.getMembersLimit())
                .accessLimit(clubDto.getAccessLimit())
                .needGrant(clubDto.getNeedGrant())
                .regDate(LocalDate.now())
                .build();

        clubRepository.save(clubEntity);

        MyClubEntity myClubEntity = MyClubEntity.builder()
                .memberId(member)
                .clubId(clubEntity)
                .authority(1)
                .regAt(LocalDate.now())
                .build();

        myClubRepository.save(myClubEntity);

        return ClubDto.of(HttpStatus.OK.value(), "소모임 생성을 완료했습니다.", clubEntity);
    }

    /**
     * 소모임 검색 기능 메서드.
     * @param clubName
     * @return
     */
    @Override
    public List<String> searchClub(Long townId, String clubName) {

        List<ClubEntity> clubEntities = clubRepository.findByClubNameContaining(clubName);
        if (clubEntities.isEmpty()) {
            throw new EntityExistsException("검색어에 해당하는 소모임이 없습니다.");
        }

        return clubEntities.stream()
                .filter(clubEntity -> clubEntity.getAccessLimit() != 2 && clubEntity.getTownId().getTownId().equals(townId))
                .map(ClubEntity::getClubName)
                .collect(Collectors.toList());
    }

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     * @param clubJoinInform
     * @return
     */
    @Transactional
    @Override
    public String joinClubRequest(ClubJoinInform clubJoinInform) {

        MemberEntity memberEntity = MemberEntity.builder()
                .memberId(clubJoinInform.getMemberId())
                .townId(clubJoinInform.getTownId())
                .build();

        MemberEntity member = memberRepository.findById(clubJoinInform.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다: " + clubJoinInform.getMemberId()));
        Optional<ClubEntity> optionalClub = clubRepository.findByClubId(clubJoinInform.getClubId());
        List<MyClubEntity> myClubEntities = myClubRepository.findByMemberId(memberEntity);

        if (optionalClub.isPresent()) {
            if (optionalClub.get().getMembersLimit() < optionalClub.get().getMembers() + 1) {
                return "인원수 제한으로 더 이상 가입이 불가능한 소모임 입니다.";
            }
            if (!optionalClub.get().getTownId().getTownId().equals(clubJoinInform.getTownId())) {
                return "동네 인증을 먼저 해주세요.";
            }
        } else {
            return "존재하지 않는 소모임 입니다.";
        }

        if (!myClubEntities.isEmpty()) {
            for (MyClubEntity myClubEntity : myClubEntities) {
                if (myClubEntity.getMyClubId().equals(clubJoinInform.getClubId())) {
                    return "이미 가입한 소모임에는 가입할 수 없습니다.";
                }
            }
        }

        if (optionalClub.get().getNeedGrant() == 1) {
            // 가입 승인 대기 관련
            return "가입 승인 대기!";
        }

        // 가입 완료
        MyClubEntity myClubEntity = MyClubEntity.builder()
                .memberId(member)
                .clubId(optionalClub.get())
                .authority(2)
                .regAt(LocalDate.now())
                .build();
        myClubRepository.save(myClubEntity);

        ClubEntity clubEntity = ClubEntity.builder()
                .clubId(optionalClub.get().getClubId())
                .clubName(optionalClub.get().getClubName())
                .townId(optionalClub.get().getTownId())
                .ownerId(optionalClub.get().getOwnerId())
                .members(optionalClub.get().getMembers() + 1)
                .membersLimit(optionalClub.get().getMembersLimit())
                .accessLimit(optionalClub.get().getAccessLimit())
                .needGrant(optionalClub.get().getNeedGrant())
                .regDate(optionalClub.get().getRegDate())
                .modDate(LocalDate.now())
                .build();
        clubRepository.save(clubEntity);

        return "가입 완료!";
    }



}
