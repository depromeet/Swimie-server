package com.depromeet.blacklist.port.out.persistence;

import com.depromeet.blacklist.domain.Blacklist;

public interface BlacklistPersistencePort {
    Blacklist save(Blacklist blacklist);

    boolean existsByMemberIdAndBlackMemberId(Long memberId, Long blackMemberId);

    void unblackMember(Long memberId, Long blackMemberId);
}
