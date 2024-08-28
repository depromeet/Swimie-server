package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import java.util.List;

public interface MemberUseCase {
    Member findById(Long id);

    Member findOrCreateMemberBy(SocialMemberCommand command);

    void deleteById(Long id);

    MemberSearchPage searchMemberByName(Long memberId, String nameQuery, Long cursorId);

    Member findByProviderId(String providerId);

    Member createMemberBy(SocialMemberCommand command);

    void checkByIdExist(List<Long> friends);
}
