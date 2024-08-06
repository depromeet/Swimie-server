package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.command.SocialMemberCommand;

public interface MemberUseCase {
    Member findById(Long id);

    Member findOrCreateMemberBy(SocialMemberCommand command);
}
