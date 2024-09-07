package com.depromeet.blacklist.port.in.usecase;

import com.depromeet.blacklist.domain.vo.BlacklistPage;
import java.util.Set;

public interface BlacklistQueryUseCase {
    boolean checkBlackMember(Long memberId, Long blackMemberId);

    BlacklistPage getBlackMembers(Long memberId, Long cursorId);

    Set<Long> getBlackMemberIds(Long memberId);

    Boolean checkBlockOrBlocked(Long loginMemberId, Long memberId);
}
