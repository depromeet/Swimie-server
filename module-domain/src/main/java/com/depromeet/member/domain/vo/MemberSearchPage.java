package com.depromeet.member.domain.vo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSearchPage {
    private List<MemberSearchInfo> members;
    private int pageSize;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public MemberSearchPage(
            List<MemberSearchInfo> members, int pageSize, Long cursorId, boolean hasNext) {
        this.members = members;
        this.pageSize = pageSize;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
