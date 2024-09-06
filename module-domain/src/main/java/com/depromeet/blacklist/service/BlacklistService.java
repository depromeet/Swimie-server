package com.depromeet.blacklist.service;

import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.blacklist.port.in.usecase.BlacklistCommandUseCase;
import com.depromeet.blacklist.port.in.usecase.BlacklistQueryUseCase;
import com.depromeet.blacklist.port.out.persistence.BlacklistPersistencePort;
import com.depromeet.member.domain.Member;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Override
    public Set<Long> getHiddenMemberIds(Long memberId) {
        List<Long> blackMemberIds = getBlackMemberIdsByMemberId(memberId);
        List<Long> memberIdsWhoBlockedMe = getMemberIdsWhoBlockedMe(memberId);

        blackMemberIds.addAll(memberIdsWhoBlockedMe);

        return new HashSet<>(blackMemberIds);
    }

    private List<Long> getBlackMemberIdsByMemberId(Long memberId) {
        return blacklistPersistencePort.findBlackMemberIdsByMemberId(memberId);
    }

    private List<Long> getMemberIdsWhoBlockedMe(Long memberId) {
        return blacklistPersistencePort.findMemberIdsByWhoBlockedMe(memberId);
    }
}
