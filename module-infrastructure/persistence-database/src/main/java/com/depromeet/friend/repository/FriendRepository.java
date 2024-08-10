package com.depromeet.friend.repository;

import static com.depromeet.friend.entity.QFriendEntity.friendEntity;

import com.depromeet.friend.domain.Friend;
import com.depromeet.friend.entity.FriendEntity;
import com.depromeet.friend.port.out.persistence.FriendPersistencePort;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepository implements FriendPersistencePort {
    private final JPAQueryFactory queryFactory;
    private final FriendJpaRepository friendJpaRepository;

    @Override
    public Long addFollowing(Friend friend) {
        return friendJpaRepository.save(FriendEntity.from(friend)).getId();
    }

    @Override
    public Optional<Friend> findById(Long id) {
        return friendJpaRepository.findById(id).map(FriendEntity::toModel);
    }

    @Override
    public Optional<Friend> findByMemberIdAndFollowingId(Long memberId, Long followingId) {
        Optional<FriendEntity> friendEntity =
                friendJpaRepository.findByMemberAndFollowing(memberId, followingId);

        return friendEntity.map(FriendEntity::toModel);
    }

    @Override
    public void deleteByMemberIdAndFollowingId(Long memberId, Long followingId) {
        queryFactory
                .delete(friendEntity)
                .where(
                        friendEntity.member.id.eq(memberId),
                        friendEntity.following.id.eq(followingId))
                .execute();
    }
}
