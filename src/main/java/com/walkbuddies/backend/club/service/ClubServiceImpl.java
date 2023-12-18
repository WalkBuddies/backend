package com.walkbuddies.backend.club.service;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubWaitingEntity;
import com.walkbuddies.backend.club.domain.MyClubEntity;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.ClubJoinInform;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.repository.ClubWaitingRepository;
import com.walkbuddies.backend.club.repository.MyClubRepository;
import com.walkbuddies.backend.exception.impl.*;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final MyClubRepository myClubRepository;
    private final MemberRepository memberRepository;
    private final ClubWaitingRepository clubWaitingRepository;

    /**
     * 소모임을 생성하는 메서드.
     *
     * @param clubDto
     * @return
     */
    @Transactional
    @Override
    public ClubDto createClub(ClubDto clubDto) {

        Optional<ClubEntity> existingClub = clubRepository.findByClubName(clubDto.getClubName());
        if (existingClub.isPresent()) {
            throw new ExistsClubException();
        }

        MemberEntity member = memberRepository.findById(clubDto.getOwnerId())
                .orElseThrow(() -> new NotFoundMemberException());

        if (!clubDto.getTownId().equals(member.getTownId().getTownId())) {
            throw new NotMyTownException();
        }

        ClubEntity clubEntity = ClubEntity.builder()
                .clubName(clubDto.getClubName())
                .townId(member.getTownId())
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

        return ClubEntity.entityToDto(clubEntity);
    }

    /**
     * 소모임 검색 기능 메서드.
     * 검색어가 포함되고 검색이 되도록 한 소모임에 한에서 모두 검색이 됨.
     *
     * @param clubName
     * @return
     */
    @Override
    public List<String> searchClub(Long townId, String clubName) {

        List<ClubEntity> clubEntities = clubRepository.findByClubNameContaining(clubName);
        if (clubEntities.isEmpty()) {
            throw new NotFoundClubException();
        }

        return clubEntities.stream()
                .filter(clubEntity -> clubEntity.getAccessLimit() != 2 && clubEntity.getTownId().getTownId().equals(townId))
                .map(ClubEntity::getClubName)
                .collect(Collectors.toList());
    }

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     *
     * @param clubJoinInform
     * @return
     */
    @Transactional
    @Override
    public String joinClubRequest(ClubJoinInform clubJoinInform) {

        TownEntity townEntity = TownEntity.builder()
                .TownId(clubJoinInform.getTownId())
                .build();

        MemberEntity memberEntity = MemberEntity.builder()
                .memberId(clubJoinInform.getMemberId())
                .townId(townEntity)
                .build();

        MemberEntity member = memberRepository.findById(clubJoinInform.getMemberId())
                .orElseThrow(() -> new NotFoundMemberException());
        Optional<ClubEntity> optionalClub = clubRepository.findByClubId(clubJoinInform.getClubId());
        List<MyClubEntity> myClubEntities = myClubRepository.findByMemberId(memberEntity);

        if (optionalClub.isPresent()) {
            if (optionalClub.get().getMembersLimit() < optionalClub.get().getMembers() + 1) {
                throw new MemberLimitException();
            }
            if (!optionalClub.get().getTownId().getTownId().equals(clubJoinInform.getTownId())) {
                throw new NotMyTownException();
            }
        } else {
            throw new NotFoundClubException();
        }

        if (!myClubEntities.isEmpty()) {
            for (MyClubEntity myClubEntity : myClubEntities) {
                if (myClubEntity.getClubId().getClubId().equals(clubJoinInform.getClubId())) {
                    throw new AlreadyMemberException();
                }
            }
        }

        if (optionalClub.get().getNeedGrant() == 1) {
            // 가입 승인 대기
            ClubWaitingEntity clubWaitingEntity = ClubWaitingEntity.builder()
                    .clubId(optionalClub.get())
                    .memberId(member)
                    .message(clubJoinInform.getMessage())
                    .build();
            clubWaitingRepository.save(clubWaitingEntity);

            MyClubEntity myClubEntity = MyClubEntity.builder()
                    .memberId(member)
                    .clubId(optionalClub.get())
                    .authority(3)
                    .regAt(LocalDate.now())
                    .build();
            myClubRepository.save(myClubEntity);

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

    /**
     * 소모임 Id를 기반으로 소모임의 가입 신청자를 볼 수 있는 메서드.
     * 신청자의 memberId와 가입시 작성했던 message를 함께 볼 수 있음.
     *
     * @param clubId
     * @return
     */
    @Override
    public List<String> getClubWaitingData(Long clubId) {
        Optional<ClubEntity> optionalClub = clubRepository.findByClubId(clubId);
        List<ClubWaitingEntity> clubWaitingEntities = clubWaitingRepository.findByClubId(optionalClub);
        if (clubWaitingEntities.isEmpty()) {
            return Collections.singletonList("가입 신청자가 없습니다.");
        }

        return clubWaitingEntities.stream()
                .map(clubWaitingEntity -> String.format("memberId: %s, message: %s",
                        clubWaitingEntity.getMemberId().getMemberId(),
                        clubWaitingEntity.getMessage()))
                .collect(Collectors.toList());
    }

    /**
     * 소모임 신청자 가입 승인, 거절 메서드.
     * 승인에 대한 Boolean 타입과 정보를 받아서 승인할지 거절할지 정함.
     *
     * @param allowJoin
     * @param clubJoinInform
     * @return
     */
    @Transactional
    @Override
    public String joinClubResponse(Boolean allowJoin, ClubJoinInform clubJoinInform) {

        Optional<ClubEntity> optionalClub = clubRepository.findByClubId(clubJoinInform.getClubId());
        if (optionalClub.isEmpty()) {
            throw new NotFoundClubException();
        }

        Optional<MemberEntity> optionalMember = memberRepository.findByMemberId(clubJoinInform.getMemberId());
        if (optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }

        Optional<MyClubEntity> optionalMyClub = myClubRepository.findByClubIdAndMemberId(optionalClub.get(), optionalMember.get());
        if (optionalMyClub.isEmpty()) {
            throw new NotFoundMyClubException();
        }

        Optional<ClubWaitingEntity> optionalClubWaiting = clubWaitingRepository.findByClubIdAndMemberId(optionalClub.get(), optionalMember.get());
        if (optionalClubWaiting.isEmpty()) {
            throw new ClubJoinException();
        }

        if (!allowJoin) {
            myClubRepository.delete(optionalMyClub.get());
            clubWaitingRepository.delete(optionalClubWaiting.get());

            return clubJoinInform.getMemberId() + "유저 소모임 가입 승인 거절.";
        }

        if (optionalClub.get().getMembersLimit() < optionalClub.get().getMembers() + 1) {
            throw new MemberLimitException();
        }

        MyClubEntity myClubEntity = MyClubEntity.builder()
                .myClubId(optionalMyClub.get().getMyClubId())
                .memberId(optionalMyClub.get().getMemberId())
                .clubId(optionalMyClub.get().getClubId())
                .authority(2)
                .regAt(LocalDate.now())
                .build();

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

        myClubRepository.save(myClubEntity);
        clubRepository.save(clubEntity);
        clubWaitingRepository.delete(optionalClubWaiting.get());

        return clubJoinInform.getMemberId() + "유저 소모임 가입 승인.";
    }

}
