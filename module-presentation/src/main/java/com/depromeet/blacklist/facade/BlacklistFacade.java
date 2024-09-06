package com.depromeet.blacklist.facade;

import com.depromeet.blacklist.dto.request.BlackMemberRequest;
import com.depromeet.blacklist.port.in.usecase.BlacklistCommandUseCase;
import com.depromeet.blacklist.port.in.usecase.BlacklistQueryUseCase;
import com.depromeet.exception.BadRequestException;
import com.depromeet.type.blacklist.BlacklistErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BlacklistFacade {
    private final BlacklistQueryUseCase blacklistQueryUseCase;
    private final BlacklistCommandUseCase blacklistCommandUseCase;

    public void blackMember(Long memberId, BlackMemberRequest request) {
        Long blackMemberId = request.blackMemberId();
        boolean isAlreadyBlacked = blacklistQueryUseCase.checkBlackMember(memberId, blackMemberId);
        if (isAlreadyBlacked) {
            throw new BadRequestException(BlacklistErrorType.ALREADY_BLACKED);
        }

        blacklistCommandUseCase.blackMember(memberId, blackMemberId);
    }
}
