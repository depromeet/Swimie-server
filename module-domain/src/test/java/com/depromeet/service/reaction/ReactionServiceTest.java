package com.depromeet.service.reaction;

import static org.assertj.core.api.Assertions.*;

import com.depromeet.fixture.domain.member.MemberFixture;
import com.depromeet.fixture.domain.memory.MemoryFixture;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.Memory;
import com.depromeet.mock.reaction.FakeReactionRepository;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.port.in.command.CreateReactionCommand;
import com.depromeet.reaction.port.out.persistence.ReactionPersistencePort;
import com.depromeet.reaction.service.ReactionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReactionServiceTest {
    private ReactionPersistencePort reactionPersistencePort;
    private ReactionService reactionService;
    private Member member1;
    private Member member2;
    private Memory memory;

    @BeforeEach
    void init() {
        reactionPersistencePort = new FakeReactionRepository();
        reactionService = new ReactionService(reactionPersistencePort);

        member1 = MemberFixture.make(1L, "USER");
        member2 = MemberFixture.make(2L, "USER");
        memory = MemoryFixture.make(1L, member1, null, null, null);
    }

    @Test
    public void 응원을_저장합니다() throws Exception {
        // given
        var command = new CreateReactionCommand(memory.getId(), "🦭", "물개세요?");

        // when
        reactionService.save(member2.getId(), memory, command);

        // then
        List<Reaction> reactions = reactionService.getReactionsOfMemory(memory.getId());

        assertThat(reactions.size()).isEqualTo(1);
        assertThat(reactions.getFirst().getMember().getId()).isEqualTo(member2.getId());
        assertThat(reactions.getFirst().getMemory().getId()).isEqualTo(memory.getId());
        assertThat(reactions.getFirst().getEmoji()).isEqualTo("🦭");
        assertThat(reactions.getFirst().getComment()).isEqualTo("물개세요?");
    }
}
