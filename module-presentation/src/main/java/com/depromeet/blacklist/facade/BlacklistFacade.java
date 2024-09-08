package com.depromeet.blacklist.facade;

import com.depromeet.blacklist.domain.vo.BlacklistPage;
import com.depromeet.blacklist.dto.request.BlackMemberRequest;
import com.depromeet.blacklist.dto.response.BlackMemberResponse;
import com.depromeet.blacklist.port.in.usecase.BlacklistCommandUseCase;
import com.depromeet.blacklist.port.in.usecase.BlacklistQueryUseCase;
import com.depromeet.exception.BadRequestException;
import com.depromeet.friend.port.in.command.DeleteFollowCommand;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.type.blacklist.BlacklistErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BlacklistFacade {
    private final ApplicationEventPublisher eventPublisher;
    private final MemberUseCase memberUseCase;
    private final BlacklistQueryUseCase blacklistQueryUseCase;
    private final BlacklistCommandUseCase blacklistCommandUseCase;

    @Value("${cloud-front.domain}")
    private String domain;

    public void blackMember(Long memberId, BlackMemberRequest request) {
        Long blackMemberId = request.blackMemberId();
        if (memberId.equals(blackMemberId)) {
            throw new BadRequestException(BlacklistErrorType.CANNOT_BLACK_MYSELF);
        }
        memberUseCase.findById(memberId);

        boolean isAlreadyBlacked = blacklistQueryUseCase.checkBlackMember(memberId, blackMemberId);
        if (isAlreadyBlacked) {
            throw new BadRequestException(BlacklistErrorType.ALREADY_BLACKED);
        }

        blacklistCommandUseCase.blackMember(memberId, blackMemberId);
        eventPublisher.publishEvent(new DeleteFollowCommand(memberId, blackMemberId));
    }

    public void unblackMember(Long memberId, Long blackMemberId) {
        blacklistCommandUseCase.unblackMember(memberId, blackMemberId);
    }

    @Transactional(readOnly = true)
    public BlackMemberResponse getBlackMembers(Long memberId, Long cursorId) {
        BlacklistPage page = blacklistQueryUseCase.getBlackMembers(memberId, cursorId);
        return BlackMemberResponse.of(page, domain);
    }
}
