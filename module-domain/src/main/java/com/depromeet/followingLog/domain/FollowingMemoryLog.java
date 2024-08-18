package com.depromeet.followingLog.domain;

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
    private Member member;
    private Memory memory;
    private LocalDateTime createdAt;

    @Builder
    public FollowingMemoryLog(Long id, Member member, Memory memory, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.memory = memory;
        this.createdAt = createdAt;
    }

    public static FollowingMemoryLog from(Member member, Memory memory) {
        return FollowingMemoryLog.builder().member(member).memory(memory).build();
    }
}
