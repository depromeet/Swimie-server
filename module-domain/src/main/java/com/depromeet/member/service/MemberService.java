package com.depromeet.member.service;

import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.usecase.GoalUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.member.port.in.usecase.NameUpdateUseCase;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.type.member.MemberErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements MemberUseCase, GoalUpdateUseCase, NameUpdateUseCase {
    private final MemberPersistencePort memberPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberPersistencePort
                .findById(id)
                .orElseThrow(() -> new NotFoundException(MemberErrorType.NOT_FOUND));
    }

    @Override
    public Member findOrCreateMemberBy(SocialMemberCommand command) {
        return memberPersistencePort
                .findByEmail(command.email())
                .orElseGet(
                        () -> {
                            Member member =
                                    Member.builder()
                                            .name(command.name())
                                            .email(command.email())
                                            .role(MemberRole.USER)
                                            .build();
                            return memberPersistencePort.save(member);
                        });
    }

    @Override
    public Member updateGoal(Long memberId, Integer goal) {
        return memberPersistencePort
                .updateGoal(memberId, goal)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_GOAL_FAILED));
    }

    @Override
    public Member updateName(Long memberId, String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(MemberErrorType.NAME_CANNOT_BE_BLANK);
        }
        return memberPersistencePort
                .updateName(memberId, name)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_NAME_FAILED));
    }
}
