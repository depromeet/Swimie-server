package com.depromeet.blacklist.port.out;

import java.util.List;

public interface BlacklistPersistencePort {
    List<Long> findBlackMemberIdsByMemberId(Long memberId);

    List<Long> findMemberIdsByWhoBlockedMe(Long memberId);
}
