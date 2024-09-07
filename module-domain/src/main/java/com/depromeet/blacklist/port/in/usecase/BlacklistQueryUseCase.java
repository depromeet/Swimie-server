package com.depromeet.blacklist.port.in.usecase;

import java.util.Set;

public interface BlacklistQueryUseCase {
    boolean checkBlackMember(Long memberId, Long blackMemberId);

    Set<Long> getBlackMemberIds(Long memberId);

    Boolean checkBlockOrBlocked(Long loginMemberId, Long memberId);
}
