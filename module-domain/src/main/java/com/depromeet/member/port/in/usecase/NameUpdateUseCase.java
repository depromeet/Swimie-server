package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;

public interface NameUpdateUseCase {
    Member updateName(Long memberId, String name);
}
