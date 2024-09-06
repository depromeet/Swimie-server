package com.depromeet.blacklist.repository;

import static com.depromeet.blacklist.entity.QBlacklistEntity.blacklistEntity;

import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.blacklist.entity.BlacklistEntity;
import com.depromeet.blacklist.port.out.persistence.BlacklistPersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlacklistRepository implements BlacklistPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final BlacklistJpaRepository blacklistJpaRepository;

    @Override
    public Blacklist save(Blacklist blacklist) {
        return blacklistJpaRepository.save(BlacklistEntity.from(blacklist)).toModel();
    }

    @Override
    public boolean existsByMemberIdAndBlackMemberId(Long memberId, Long blackMemberId) {
        return blacklistJpaRepository.existsByMemberIdAndBlackMemberId(memberId, blackMemberId);
    }

    @Override
    public void unblackMember(Long memberId, Long blackMemberId) {
        blacklistJpaRepository.deleteByMemberIdAndBlackMemberId(memberId, blackMemberId);
    }

    @Override
    public List<Long> findBlackMemberIdsByMemberId(Long memberId) {
        return queryFactory
                .select(blacklistEntity.blackMember.id)
                .from(blacklistEntity)
                .where(blacklistEntity.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Long> findMemberIdsByWhoBlockedMe(Long memberId) {
        return queryFactory
                .select(blacklistEntity.member.id)
                .from(blacklistEntity)
                .where(blacklistEntity.blackMember.id.eq(memberId))
                .fetch();
    }
}
