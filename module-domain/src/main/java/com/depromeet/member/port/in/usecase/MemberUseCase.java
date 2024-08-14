package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.command.UpdateMemberCommand;

public interface MemberUseCase {
    Member findById(Long id);

    Member findOrCreateMemberBy(SocialMemberCommand command);

    void deleteById(Long id);

    MemberSearchPage searchMemberByName(String nameQuery, Long cursorId);

    Member update(UpdateMemberCommand command);
}
