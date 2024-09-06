package com.depromeet.blacklist.port.in.usecase;

import com.depromeet.blacklist.domain.Blacklist;

public interface BlacklistCommandUseCase {
    Blacklist blackMember(Long memberId, Long blackMemberId);
}
