package com.depromeet.blacklist.domain.vo;

import com.depromeet.member.domain.Member;
import java.util.List;

public record BlacklistPage(List<Member> blackMembers, boolean hasNext, Long cursorId) {
    public static BlacklistPage of(List<Member> blackMembers, boolean hasNext, Long nextCursorId) {
        return new BlacklistPage(blackMembers, hasNext, nextCursorId);
    }
}
