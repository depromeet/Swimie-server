package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;

public interface MemberUpdateUseCase {
    Member updateNickname(Long memberId, String nickname);

    Member updateGender(Long memberId, MemberGender gender);
}
