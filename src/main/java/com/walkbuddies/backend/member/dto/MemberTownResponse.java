package com.walkbuddies.backend.member.dto;

import com.walkbuddies.backend.member.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTownResponse {
    private String email;
    private String name;
    private Long townId;
    private String townName;

    public MemberTownResponse(MemberEntity member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.townId = member.getTownId().getTownId();
        this.townName = member.getTownId().getTownName();
    }
}
