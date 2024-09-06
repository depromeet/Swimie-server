package com.depromeet.blacklist.port.in;

import java.util.Set;

public interface BlacklistGetUseCase {
    Set<Long> getHiddenMemberIds(Long memberId);
}
