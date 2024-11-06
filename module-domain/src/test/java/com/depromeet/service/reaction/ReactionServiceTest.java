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
    private Reaction reaction;

    @BeforeEach
    void init() {
        reactionPersistencePort = new FakeReactionRepository();
        reactionService = new ReactionService(reactionPersistencePort);

        member1 = MemberFixture.make(1L, "USER");
        member2 = MemberFixture.make(2L, "USER");
        memory = MemoryFixture.make(1L, member1, null, null, null);

        var command = new CreateReactionCommand(memory.getId(), "🔥", "오늘도 힘내요!");
        reaction = reactionService.save(member2.getId(), memory, command);
    }

    @Test
    public void 응원을_저장합니다() throws Exception {
        // given
        var command = new CreateReactionCommand(memory.getId(), "🦭", "물개세요?");

        // when
        reactionService.save(member2.getId(), memory, command);

        // then
        List<Reaction> reactions = reactionService.getReactionsOfMemory(memory.getId());

        assertThat(reactions.size()).isEqualTo(2);
        assertThat(reactions.getLast().getMember().getId()).isEqualTo(member2.getId());
        assertThat(reactions.getLast().getMemory().getId()).isEqualTo(memory.getId());
        assertThat(reactions.getLast().getEmoji()).isEqualTo("🦭");
        assertThat(reactions.getLast().getComment()).isEqualTo("물개세요?");
    }

    @Test
    public void 응원을_삭제합니다() throws Exception {
        // when
        reactionService.deleteById(member1.getId(), 1L);

        // then
        List<Reaction> reactions = reactionService.getReactionsOfMemory(memory.getId());
        assertThat(reactions.size()).isEqualTo(0);
    }

    @Test
    public void 기록에_해당하는_응원을_모두_삭제합니다() throws Exception {
        // when
        reactionService.deleteAllById(List.of(reaction.getId()));

        // then
        List<Reaction> reactions = reactionService.getReactionsOfMemory(memory.getId());
        assertThat(reactions.size()).isEqualTo(0);
    }

    @Test
    public void 기록에_해당하는_응원을_모두_조회합니다() throws Exception {
        // given
        Long memoryId = memory.getId();

        // when
        List<Reaction> reactions = reactionService.getReactionsOfMemory(memoryId);

        // then
        assertThat(reactions.size()).isEqualTo(1);
        assertThat(reactions.getFirst().getMember().getId()).isEqualTo(member2.getId());
        assertThat(reactions.getFirst().getMemory().getId()).isEqualTo(memoryId);
        assertThat(reactions.getFirst().getEmoji()).isEqualTo("🔥");
        assertThat(reactions.getFirst().getComment()).isEqualTo("오늘도 힘내요!");
    }

    @Test
    public void 응원을_페이지_단위로_조회합니다() throws Exception {
        // given
        var command = new CreateReactionCommand(memory.getId(), "🦭", "물개세요?");
        reactionService.save(member2.getId(), memory, command);

        // when
        var reactionPage = reactionService.getDetailReactions(memory.getId(), null);

        // then
        assertThat(reactionPage.getReactions().size()).isEqualTo(2);
        assertThat(reactionPage.getReactions().getFirst().getMember().getId())
                .isEqualTo(member2.getId());
        assertThat(reactionPage.getReactions().getFirst().getMemory().getId())
                .isEqualTo(memory.getId());
        assertThat(reactionPage.getReactions().getFirst().getEmoji()).isEqualTo("🦭");
        assertThat(reactionPage.getReactions().getFirst().getComment()).isEqualTo("물개세요?");
    }

    @Test
    public void 기록의_총_응원수를_조회합니다() throws Exception {
        // given
        Long memoryId = memory.getId();

        // when
        Long reactionCount = reactionService.getDetailReactionsCount(memoryId);

        // then
        assertThat(reactionCount).isEqualTo(1L);
    }

    @Test
    public void 여러_기록에_해당하는_응원수를_조회합니다() throws Exception {
        // given
        var memory2 = MemoryFixture.make(2L, member1, null, null, null);
        var command = new CreateReactionCommand(memory2.getId(), "💪🏻", "힘내자!");
        var command2 = new CreateReactionCommand(memory2.getId(), "💪🏻", "힘내자!");
        reactionService.save(member2.getId(), memory2, command);
        reactionService.save(member2.getId(), memory2, command2);

        // when
        var reactionCounts =
                reactionService.getDetailReactionsCountByMemoryIds(
                        List.of(memory.getId(), memory2.getId()));

        // then
        assertThat(reactionCounts.size()).isEqualTo(2);
        assertThat(reactionCounts.getFirst().getMemoryId()).isEqualTo(memory.getId());
        assertThat(reactionCounts.getFirst().getReactionCount()).isEqualTo(1L);

        assertThat(reactionCounts.getLast().getMemoryId()).isEqualTo(memory2.getId());
        assertThat(reactionCounts.getLast().getReactionCount()).isEqualTo(2L);
    }
}
