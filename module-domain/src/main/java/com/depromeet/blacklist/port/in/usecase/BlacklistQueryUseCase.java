package com.depromeet.blacklist.port.in.usecase;

import com.depromeet.blacklist.domain.vo.BlacklistPage;

public interface BlacklistQueryUseCase {
    boolean checkBlackMember(Long memberId, Long blackMemberId);

    BlacklistPage getBlackMembers(Long memberId, Long cursorId);
}
