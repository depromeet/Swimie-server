package com.depromeet.friend.domain;

import com.depromeet.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Friend {
    private Long id;
    private Member member;
    private Member following;
    private LocalDateTime createdAt;

    @Builder
    public Friend(Long id, Member member, Member following, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.following = following;
        this.createdAt = createdAt;
    }
}
