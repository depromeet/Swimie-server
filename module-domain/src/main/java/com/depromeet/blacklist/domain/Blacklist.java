package com.depromeet.blacklist.domain;

import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Blacklist {
    private Long id;
    private Member member;
    private Member blackTarget;
    private LocalDateTime createdAt;

    @Builder
    public Blacklist(Long id, Member member, Member blackTarget, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.blackTarget = blackTarget;
        this.createdAt = createdAt;
    }
}
