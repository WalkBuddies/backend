package com.walkbuddies.backend.member.security;

import com.walkbuddies.backend.exception.impl.NotFoundMemberException;
import com.walkbuddies.backend.member.domain.MemberEntity;
import com.walkbuddies.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity member = findByEmail(email);

        return new MemberDetails(member, member.getEmail());
    }

    private MemberEntity findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);
    }
}
