package com.depromeet.followingLog.domain;

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
    private LocalDateTime createdAt;

    @Builder
    public FollowingMemoryLog(Long id, Memory memory, LocalDateTime createdAt) {
        this.id = id;
        this.memory = memory;
        this.createdAt = createdAt;
    }

    public static FollowingMemoryLog from(Memory memory) {
        return FollowingMemoryLog.builder().memory(memory).build();
    }
}
