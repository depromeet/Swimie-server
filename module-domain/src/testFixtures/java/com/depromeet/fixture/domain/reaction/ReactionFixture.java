package com.depromeet.fixture.domain.reaction;

import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.reaction.domain.Reaction;
import java.time.LocalDateTime;

public class ReactionFixture {
    public static Reaction make(Member member, Memory memory) {
        return Reaction.builder()
                .member(member)
                .memory(memory)
                .emoji("🦭")
                .comment("물개세요?")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
