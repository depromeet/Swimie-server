package com.depromeet.blacklist.repository;

import static com.depromeet.blacklist.entity.QBlacklistEntity.blacklistEntity;

import com.depromeet.blacklist.port.out.BlacklistPersistencePort;
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
