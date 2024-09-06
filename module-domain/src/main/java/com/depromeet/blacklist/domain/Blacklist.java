package com.depromeet.blacklist.domain;

import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Blacklist {
    private Long id;
    private Member member;
    private Member blackMember;
    private LocalDateTime createdAt;

    @Builder
    public Blacklist(Long id, Member member, Member blackMember, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.blackMember = blackMember;
        this.createdAt = createdAt;
    }
}
