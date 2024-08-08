package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;

public interface MemberUpdateUseCase {
    Member updateName(Long memberId, String name);

    Member updateGender(Long memberId, MemberGender gender);
}
