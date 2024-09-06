package com.depromeet.blacklist.repository;

import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.blacklist.entity.BlacklistEntity;
import com.depromeet.blacklist.port.out.persistence.BlacklistPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlacklistRepository implements BlacklistPersistencePort {
    private final BlacklistJpaRepository blacklistJpaRepository;

    @Override
    public Blacklist save(Blacklist blacklist) {
        return blacklistJpaRepository.save(BlacklistEntity.from(blacklist)).toModel();
    }

    @Override
    public boolean existsByMemberIdAndBlackMemberId(Long memberId, Long blackMemberId) {
        return blacklistJpaRepository.existsByMemberIdAndBlackMemberId(memberId, blackMemberId);
    }
}
