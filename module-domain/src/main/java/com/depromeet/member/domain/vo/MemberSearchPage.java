package com.depromeet.member.domain.vo;

import com.depromeet.member.domain.Member;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSearchPage {
    private List<Member> members;
    private int pageSize;
    private Long cursorId;
    private boolean hasNext;

    @Builder
    public MemberSearchPage(List<Member> members, int pageSize, Long cursorId, boolean hasNext) {
        this.members = members;
        this.pageSize = pageSize;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }
}
