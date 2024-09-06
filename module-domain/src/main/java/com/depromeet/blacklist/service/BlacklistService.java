package com.depromeet.blacklist.service;

import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.blacklist.port.in.usecase.BlacklistCommandUseCase;
import com.depromeet.blacklist.port.in.usecase.BlacklistQueryUseCase;
import com.depromeet.blacklist.port.out.persistence.BlacklistPersistencePort;
import com.depromeet.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlacklistService implements BlacklistQueryUseCase, BlacklistCommandUseCase {
    private final BlacklistPersistencePort blacklistPersistencePort;

    @Override
    @Transactional
    public Blacklist blackMember(Long memberId, Long blackMemberId) {
        return blacklistPersistencePort.save(
                Blacklist.builder()
                        .member(Member.builder().id(memberId).build())
                        .blackMember(Member.builder().id(blackMemberId).build())
                        .build());
    }

    @Override
    @Transactional
    public void unblackMember(Long memberId, Long blackMemberId) {
        blacklistPersistencePort.unblackMember(memberId, blackMemberId);
    }

    @Override
    public boolean checkBlackMember(Long memberId, Long blackMemberId) {
        return blacklistPersistencePort.existsByMemberIdAndBlackMemberId(memberId, blackMemberId);
    }
}
