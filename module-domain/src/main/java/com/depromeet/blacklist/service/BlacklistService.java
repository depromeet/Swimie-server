package com.depromeet.blacklist.service;

import com.depromeet.blacklist.port.in.BlacklistGetUseCase;
import com.depromeet.blacklist.port.out.BlacklistPersistencePort;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlacklistService implements BlacklistGetUseCase {
    private final BlacklistPersistencePort blacklistPersistencePort;

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
