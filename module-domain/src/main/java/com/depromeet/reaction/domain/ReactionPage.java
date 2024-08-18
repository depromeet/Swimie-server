package com.depromeet.reaction.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReactionPage {
    private List<Reaction> reactions;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public ReactionPage(List<Reaction> reactions, Long cursorId, boolean hasNext) {
        this.reactions = reactions != null ? reactions : new ArrayList<>();
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }

    public static ReactionPage of(List<Reaction> reactions, Long cursorId, boolean hasNext) {
        return ReactionPage.builder()
                .reactions(reactions)
                .cursorId(cursorId)
                .hasNext(hasNext)
                .build();
    }
}
