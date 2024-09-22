package com.depromeet.followinglog.domain;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FollowingMemoryLog {
    private Long id;
    private Memory memory;
    private Member member;
    private LocalDateTime createdAt;

    @Builder
    public FollowingMemoryLog(Long id, Memory memory, Member member, LocalDateTime createdAt) {
        this.id = id;
        this.memory = memory;
        this.member = member;
        this.createdAt = createdAt;
    }

    public static FollowingMemoryLog from(Memory memory, Member member) {
        return FollowingMemoryLog.builder().memory(memory).member(member).build();
    }
}
