package com.walkbuddies.backend.club.service.impl;

import com.walkbuddies.backend.club.domain.ClubEntity;
import com.walkbuddies.backend.club.domain.ClubPreface;
import com.walkbuddies.backend.club.domain.ClubWaitingEntity;
import com.walkbuddies.backend.club.domain.MyClubEntity;
import com.walkbuddies.backend.club.domain.TownEntity;
import com.walkbuddies.backend.club.dto.ClubDto;
import com.walkbuddies.backend.club.dto.form.*;
import com.walkbuddies.backend.club.dto.ClubPrefaceDto;
import com.walkbuddies.backend.club.dto.PrefaceConvertDtoEntity;
import com.walkbuddies.backend.club.repository.ClubPrefaceRepository;
import com.walkbuddies.backend.club.repository.ClubRepository;
import com.walkbuddies.backend.club.repository.ClubWaitingRepository;
import com.walkbuddies.backend.club.repository.MyClubRepository;
import com.walkbuddies.backend.club.repository.TownRepository;
import com.walkbuddies.backend.club.service.ClubService;
import com.walkbuddies.backend.exception.impl.*;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import com.walkbuddies.backend.type.ClubAccessLimit;
import com.walkbuddies.backend.type.ClubNeedGrant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final TownRepository townRepository;
    private final PrefaceConvertDtoEntity prefaceConvert;
    private final ClubPrefaceRepository clubPrefaceRepository;

    /**
     * 소모임을 생성하는 메서드.
     *
     * @param clubCreateParameter
     * @return
     */
    @Transactional
    @Override
    public ClubResponse create(ClubCreateParameter clubCreateParameter) {

        Optional<ClubEntity> existingClub = clubRepository.findByClubName(clubCreateParameter.getClubName());
        if (existingClub.isPresent()) {
            throw new ExistsClubException();
        }

        MemberEntity member = getMemberEntity(clubCreateParameter.getOwnerId());
        if (clubCreateParameter.getTownId() != member.getTownId().getTownId()) {
            throw new NotMyTownException();
        }

        ClubEntity clubEntity = ClubEntity.builder()
                .clubName(clubCreateParameter.getClubName())
                .town(member.getTownId())
                .owner(member)
                .members(1)
                .membersLimit(clubCreateParameter.getMembersLimit())
                .accessLimit(clubCreateParameter.getAccessLimit())
                .needGrant(clubCreateParameter.getNeedGrant())
                .regDate(LocalDateTime.now())
                .build();

        clubRepository.save(clubEntity);

        MyClubEntity myClubEntity = MyClubEntity.builder()
                .memberId(member)
                .clubId(clubEntity)
                .authority(1)
                .regDate(LocalDate.now())
                .build();

        myClubRepository.save(myClubEntity);

//        ClubResponse clubResponse = ClubResponse.builder()
//                .clubName(clubEntity.getClubName())
//                .ownerName(clubEntity.getOwner().getName())
//                .townName(clubEntity.getTown().getTownName())
//                .members(clubEntity.getMembers())
//                .membersLimit(clubEntity.getMembersLimit())
//                .accessLimit(clubEntity.getAccessLimit())
//                .needGrant(clubEntity.getNeedGrant())
//                .isSuspended(clubEntity.isSuspended())
//                .regDate(clubEntity.getRegDate())
//                .build();

        return ClubResponse.of(clubEntity);
    }

    /**
     * 소모임 폐쇄 메서드
     * @param clubParameter
     * @return
     */
    @Transactional
    @Override
    public ClubResponse delete(ClubParameter clubParameter) {

        ClubEntity clubEntity = getClubEntity(clubParameter.getClubId());
        if (clubEntity.isSuspended()) {
            throw new ClubSuspendedException();
        }
        if (clubEntity.getOwner().getMemberId() != clubParameter.getOwnerId()) {
            throw new NotClubAdminException();
        }

        MemberEntity memberEntity = getMemberEntity(clubParameter.getOwnerId());
        List<MyClubEntity> myClubEntities = myClubRepository.findByClubId(clubEntity);
        List<ClubWaitingEntity> clubWaitingEntities = clubWaitingRepository.findByClubId(clubEntity);
        Optional<ClubEntity> byClubIdAndOwner = clubRepository.findByClubIdAndOwner(clubParameter.getClubId(), memberEntity);

        if (!clubWaitingEntities.isEmpty()) {
            for (ClubWaitingEntity clubWaitingEntity : clubWaitingEntities) {
                clubWaitingRepository.delete(clubWaitingEntity);
            }
        }

        for (MyClubEntity myClubEntity : myClubEntities) {
            myClubRepository.delete(myClubEntity);
        }

        clubRepository.delete(byClubIdAndOwner.get());

//        ClubResponse clubResponse = ClubResponse.builder()
//                .clubName(clubEntity.getClubName())
//                .ownerName(clubEntity.getOwner().getName())
//                .townName(clubEntity.getTown().getTownName())
//                .members(clubEntity.getMembers())
//                .membersLimit(clubEntity.getMembersLimit())
//                .accessLimit(clubEntity.getAccessLimit())
//                .needGrant(clubEntity.getNeedGrant())
//                .isSuspended(clubEntity.isSuspended())
//                .regDate(clubEntity.getRegDate())
//                .build();

        return ClubResponse.of(clubEntity);
    }

    /**
     * 소모임 검색 기능 메서드.
     * 검색어가 포함되고 검색이 되도록 한 소모임에 한에서 모두 검색이 됨.
     *
     * @param clubParameter
     * @return
     */
    @Override
    public List<String> search(ClubParameter clubParameter) {

        List<ClubEntity> clubEntities = clubRepository.findByClubNameContaining(clubParameter.getClubName());
        if (clubEntities.isEmpty()) {
            throw new NotFoundClubException();
        }

        return clubEntities.stream()
                .filter(clubEntity -> clubEntity.getAccessLimit() != ClubAccessLimit.SEARCH_NOT_ALLOW.getValue()
                        && clubEntity.getTown().getTownId().equals(clubParameter.getTownId()))
                .map(ClubEntity::getClubName)
                .collect(Collectors.toList());
    }

    /**
     * 소모임 가입 요청 기능 메서드
     * 단 가입 조건이 없는 소모임일 경우 바로 가입 가능
     *
     * @param clubJoinParameter
     * @return
     */
    @Transactional
    @Override
    public String joinRequest(ClubJoinParameter clubJoinParameter) {

        ClubEntity clubEntity = getClubEntity(clubJoinParameter.getClubId());
        if (clubEntity.isSuspended()) {
            throw new ClubSuspendedException();
        }
        if (clubEntity.getMembersLimit() < clubEntity.getMembers() + 1) {
            throw new MemberLimitException();
        }
        if (!clubEntity.getTown().getTownId().equals(clubJoinParameter.getTownId())) {
            throw new NotMyTownException();
        }

        MemberEntity memberEntity = getMemberEntity(clubJoinParameter.getMemberId());

        Optional<MyClubEntity> optionalMyClub = myClubRepository.findByClubIdAndMemberId(clubEntity, memberEntity);
        if (optionalMyClub.isPresent()) {
            throw new AlreadyMemberException();
        }

        if (clubEntity.getNeedGrant() == ClubNeedGrant.GRANT_NEED.getValue()) {
            ClubWaitingEntity clubWaitingEntity = ClubWaitingEntity.builder()
                    .clubId(clubEntity)
                    .memberId(memberEntity)
                    .nickName(memberEntity)
                    .message(clubJoinParameter.getMessage())
                    .build();
            clubWaitingRepository.save(clubWaitingEntity);

            MyClubEntity myClubEntity = MyClubEntity.builder()
                    .memberId(memberEntity)
                    .clubId(clubEntity)
                    .authority(3)
                    .regDate(LocalDate.now())
                    .build();
            myClubRepository.save(myClubEntity);

            return "가입 승인 대기!";
        }

        MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                .memberId(memberEntity)
                .clubId(clubEntity)
                .authority(2)
                .regDate(LocalDate.now())
                .build();
        myClubRepository.save(updatedMyClubEntity);

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .town(clubEntity.getTown())
                .owner(clubEntity.getOwner())
                .members(clubEntity.getMembers() + 1)
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDateTime.now())
                .build();
        clubRepository.save(updatedClubEntity);

        return "가입 완료!";
    }

    /**
     * 소모임 Id를 기반으로 소모임의 가입 신청자를 볼 수 있는 메서드.
     * 신청자의 memberId와 가입시 작성했던 message를 함께 볼 수 있음.
     *
     * @param clubParameter
     * @return
     */
    @Override
    public List<String> getWaitingData(ClubParameter clubParameter) {

        ClubEntity clubEntity = getClubEntity(clubParameter.getClubId());
        if (clubEntity.isSuspended()) {
            throw new ClubSuspendedException();
        }

        List<ClubWaitingEntity> clubWaitingEntities = clubWaitingRepository.findByClubId(clubEntity);
        if (clubWaitingEntities.isEmpty()) {
            return Collections.singletonList("가입 신청자가 없습니다.");
        }

        return clubWaitingEntities.stream()
                .map(clubWaitingEntity -> String.format("닉네임: %s, 메시지: %s",
                        clubWaitingEntity.getNickName().getNickname(),
                        clubWaitingEntity.getMessage()))
                .collect(Collectors.toList());
    }

    /**
     * 소모임 신청자 가입 승인, 거절 메서드.
     * 승인에 대한 Boolean 타입과 정보를 받아서 승인할지 거절할지 정함.
     *
     * @param clubJoinParameter
     * @return
     */
    @Transactional
    @Override
    public String joinResponse(ClubJoinParameter clubJoinParameter) {

        ClubEntity clubEntity = getClubEntity(clubJoinParameter.getClubId());
        if (clubEntity.isSuspended()) {
            throw new ClubSuspendedException();
        }
        if (clubEntity.getMembersLimit() < clubEntity.getMembers() + 1) {
            throw new MemberLimitException();
        }

        MemberEntity memberEntity = getMemberEntity(clubJoinParameter.getMemberId());
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);
        ClubWaitingEntity clubWaitingEntity = getClubWaitingEntity(clubEntity, memberEntity);

        if (!clubJoinParameter.isAllowJoin()) {
            myClubRepository.delete(myClubEntity);
            clubWaitingRepository.delete(clubWaitingEntity);
            return "닉네임: " + memberEntity.getNickname() + " 유저 소모임 가입 승인 거절.";
        }

        MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                .myClubId(myClubEntity.getMyClubId())
                .memberId(myClubEntity.getMemberId())
                .clubId(myClubEntity.getClubId())
                .authority(2)
                .regDate(LocalDate.now())
                .build();
        myClubRepository.save(updatedMyClubEntity);

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .town(clubEntity.getTown())
                .owner(clubEntity.getOwner())
                .members(clubEntity.getMembers() + 1)
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDateTime.now())
                .build();
        clubRepository.save(updatedClubEntity);

        clubWaitingRepository.delete(clubWaitingEntity);

        return "닉네임: " + memberEntity.getNickname() + " 유저 소모임 가입 승인.";
    }

    /**
     * 소모임 탈퇴 메서드.
     *
     * @param clubParameter
     * @return
     */
    @Transactional
    @Override
    public String leave(ClubParameter clubParameter) {

        ClubEntity clubEntity = getClubEntity(clubParameter.getClubId());
        if (clubEntity.isSuspended()) {
            throw new ClubSuspendedException();
        }

        if (clubEntity.getOwner().getMemberId() == clubParameter.getMemberId()) {
            throw new ChangeClubOwnerException();
        }

        MemberEntity memberEntity = getMemberEntity(clubParameter.getMemberId());
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);

        myClubRepository.delete(myClubEntity);

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubEntity.getClubId())
                .clubName(clubEntity.getClubName())
                .town(clubEntity.getTown())
                .owner(clubEntity.getOwner())
                .members(clubEntity.getMembers() - 1)
                .membersLimit(clubEntity.getMembersLimit())
                .accessLimit(clubEntity.getAccessLimit())
                .needGrant(clubEntity.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDateTime.now())
                .build();
        clubRepository.save(updatedClubEntity);

        return "소모임 " + clubEntity.getClubName() + "에서 탈퇴 완료되었습니다.";
    }

    /**
     * 소모임 정보를 수정하는 메서드
     *
     * @param clubUpdateParameter
     * @return
     */
    @Transactional
    @Override
    public ClubResponse update(ClubUpdateParameter clubUpdateParameter) {

        ClubEntity clubEntity = getClubEntity(clubUpdateParameter.getClubId());
        if (clubUpdateParameter.getMemberId() != clubEntity.getOwner().getMemberId()) {
            throw new NotClubAdminException();
        }
        if (clubEntity.isSuspended()) {
            throw new ClubSuspendedException();
        }

        MemberEntity memberEntity = getMemberEntity(clubUpdateParameter.getOwnerId());
        MyClubEntity myClubEntity = getMyClubEntity(clubEntity, memberEntity);
        TownEntity townEntity = townRepository.findByTownId(clubUpdateParameter.getTownId()).get();

        if (clubEntity.getOwner().getMemberId() != clubUpdateParameter.getOwnerId()) {
            MemberEntity member = getMemberEntity(clubEntity.getOwner().getMemberId());
            MyClubEntity myClub = getMyClubEntity(clubEntity, member);

            MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                    .myClubId(myClub.getMyClubId())
                    .memberId(member)
                    .clubId(clubEntity)
                    .authority(2)
                    .regDate(myClub.getRegDate())
                    .build();
            myClubRepository.save(updatedMyClubEntity);
        }

        ClubEntity updatedClubEntity = ClubEntity.builder()
                .clubId(clubUpdateParameter.getClubId())
                .clubName(clubUpdateParameter.getClubName())
                .town(townEntity)
                .owner(memberEntity)
                .members(clubEntity.getMembers())
                .membersLimit(clubUpdateParameter.getMembersLimit())
                .accessLimit(clubUpdateParameter.getAccessLimit())
                .needGrant(clubUpdateParameter.getNeedGrant())
                .regDate(clubEntity.getRegDate())
                .modDate(LocalDateTime.now())
                .build();
        clubRepository.save(updatedClubEntity);

        MyClubEntity updatedMyClubEntity = MyClubEntity.builder()
                .myClubId(myClubEntity.getMyClubId())
                .memberId(memberEntity)
                .clubId(clubEntity)
                .authority(1)
                .regDate(myClubEntity.getRegDate())
                .build();
        myClubRepository.save(updatedMyClubEntity);

        return ClubResponse.of(updatedClubEntity);
    }

    /**
     * 내 소모임 목록 보기 메서드
     *
     * @param clubParameter
     * @return
     */
    @Override
    public List<ClubResponse> getMyClub(ClubParameter clubParameter) {

        MemberEntity memberEntity = getMemberEntity(clubParameter.getMemberId());
        List<MyClubEntity> myClubEntities = myClubRepository.findByMemberId(memberEntity);
        if (myClubEntities.isEmpty()) {
            throw new NotFoundMyClubException();
        }

        List<ClubResponse> clubResponses = new ArrayList<>();
        for (int i = 0; i < myClubEntities.size(); i++) {
            ClubEntity clubEntity = getClubEntity(myClubEntities.get(i).getClubId().getClubId());
            clubResponses.add(ClubResponse.of(clubEntity));
        }

        return clubResponses;
    }

    /**
     * 말머리 생성
     * @param clubPrefaceDto
     * @return
     */
    @Override
    public ClubPrefaceDto createPreface(ClubPrefaceDto clubPrefaceDto) {
        ClubPreface entity = prefaceConvert.prefaceDtoToEntity(clubPrefaceDto);
        clubPrefaceRepository.save(entity);

        return prefaceConvert.prefaceEntityToDto(entity);
    }

    /**
     * 말머리 수정
     * @param clubPrefaceDto
     * @return
     */
    @Override
    public ClubPrefaceDto updatePreface(ClubPrefaceDto clubPrefaceDto) {
               ClubPreface entity = getPrefaceEntity(clubPrefaceDto.getPrefaceId());
        entity.update(clubPrefaceDto);
        clubPrefaceRepository.save(entity);
        return prefaceConvert.prefaceEntityToDto(entity);
    }

    /**
     * 말머리 삭제
     * @param prefaceId
     */
    @Override
    public void deletePreface(Long prefaceId) {
        ClubPreface entity = getPrefaceEntity(prefaceId);
        clubPrefaceRepository.delete(entity);

    }
    private ClubPreface getPrefaceEntity(Long prefaceId) {
        Optional<ClubPreface> op = clubPrefaceRepository.findByPrefaceId(prefaceId);
        if (op.isEmpty()) {
            throw new NotFoundPrefaceException();
        }
        return op.get();
    }

    private ClubEntity getClubEntity(Long clubId) {
        return clubRepository.findByClubId(clubId)
                .orElseThrow(NotFoundClubException::new);
    }

    private MemberEntity getMemberEntity(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(NotFoundMemberException::new);
    }

    private MyClubEntity getMyClubEntity(ClubEntity clubEntity, MemberEntity memberEntity) {
        return myClubRepository.findByClubIdAndMemberId(clubEntity, memberEntity)
                .orElseThrow(NotFoundMyClubException::new);
    }

    private ClubWaitingEntity getClubWaitingEntity(ClubEntity clubEntity, MemberEntity memberEntity) {
        return clubWaitingRepository.findByClubIdAndMemberId(clubEntity, memberEntity)
                .orElseThrow(ClubJoinException::new);
    }

}
