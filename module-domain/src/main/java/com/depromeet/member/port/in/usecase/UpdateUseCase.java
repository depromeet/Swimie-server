package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;

public interface UpdateUseCase {
    Member updateName(Long memberId, String name);

    Member updateGender(Long memberId, MemberGender gender);
}
