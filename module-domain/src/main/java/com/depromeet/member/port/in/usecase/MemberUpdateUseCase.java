package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.port.in.command.UpdateMemberCommand;

public interface MemberUpdateUseCase {

    Member update(UpdateMemberCommand command);

    Member updateNickname(Long memberId, String nickname);

    Member updateGender(Long memberId, MemberGender gender);

    Member updateProfileImageUrl(Long memberId, String profileImageUrl);
}
