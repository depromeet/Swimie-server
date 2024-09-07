package com.depromeet.member.domain.vo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSearchPage {
    private List<MemberSearchInfo> members;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public MemberSearchPage(List<MemberSearchInfo> members, Long cursorId, boolean hasNext) {
        this.members = members;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
