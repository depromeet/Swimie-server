package com.depromeet.member.service;

import com.depromeet.auth.port.out.persistence.RefreshRedisPersistencePort;
import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.domain.vo.MemberSearchPage;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.command.UpdateMemberCommand;
import com.depromeet.member.port.in.usecase.GoalUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.type.member.MemberErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements MemberUseCase, GoalUpdateUseCase, MemberUpdateUseCase {
    private final MemberPersistencePort memberPersistencePort;
    private final RefreshRedisPersistencePort refreshRedisPersistencePort;

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
                .findByProviderId(command.providerId())
                .orElseGet(
                        () -> {
                            Member member =
                                    Member.builder()
                                            .nickname(command.name())
                                            .email(command.email())
                                            .role(MemberRole.USER)
                                            .providerId(command.providerId())
                                            .build();
                            return memberPersistencePort.save(member);
                        });
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        memberPersistencePort.deleteById(id);
        refreshRedisPersistencePort.deleteData(id);
    }

    @Override
    public MemberSearchPage searchMemberByName(Long memberId, String nameQuery, Long cursorId) {
        return memberPersistencePort.searchByNameQuery(memberId, nameQuery, cursorId);
    }

    @Override
    public Member findByProviderId(String providerId) {
        return memberPersistencePort.findByProviderId(providerId).orElse(null);
    }

    @Override
    public Member createMemberBy(SocialMemberCommand command) {
        Member member =
                Member.builder()
                        .nickname(command.name())
                        .email(command.email())
                        .role(MemberRole.USER)
                        .providerId(command.providerId())
                        .build();
        return memberPersistencePort.save(member);
    }

    @Override
    public Member update(UpdateMemberCommand command) {
        return memberPersistencePort
                .update(command)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_FAILED));
    }

    @Override
    public Member updateGoal(Long memberId, Integer goal) {
        return memberPersistencePort
                .updateGoal(memberId, goal)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_GOAL_FAILED));
    }

    @Override
    public Member updateNickname(Long memberId, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new BadRequestException(MemberErrorType.NAME_CANNOT_BE_BLANK);
        }
        return memberPersistencePort
                .updateNickname(memberId, nickname)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_NAME_FAILED));
    }

    @Override
    public Member updateGender(Long memberId, MemberGender gender) {
        if (gender == null) {
            throw new BadRequestException(MemberErrorType.GENDER_CANNOT_BE_BLANK);
        }
        return memberPersistencePort
                .updateGender(memberId, gender)
                .orElseThrow(
                        () -> new InternalServerException(MemberErrorType.UPDATE_GENDER_FAILED));
    }

    @Override
    public Member updateProfileImageUrl(Long memberId, String profileImageUrl) {
        return memberPersistencePort
                .updateProfileImageUrl(memberId, profileImageUrl)
                .orElseThrow(
                        () ->
                                new InternalServerException(
                                        MemberErrorType.UPDATE_PROFILE_IMAGE_FAILED));
    }
}
