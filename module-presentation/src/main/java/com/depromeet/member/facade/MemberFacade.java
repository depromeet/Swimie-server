package com.depromeet.member.facade;

import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.member.port.in.usecase.NameUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {
    private final MemberUseCase memberUseCase;
    private final NameUpdateUseCase nameUpdateUseCase;

    public Member findById(Long memberId) {
        return memberUseCase.findById(memberId);
    }

    public Member findOrCreateMemberBy(SocialMemberCommand command) {
        return memberUseCase.findOrCreateMemberBy(command);
    }

    public Member updateName(Long memberId, String name) {
        return nameUpdateUseCase.updateName(memberId, name);
    }
}
