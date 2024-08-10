package com.depromeet.friend.repository;

import com.depromeet.friend.entity.FriendEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendJpaRepository extends JpaRepository<FriendEntity, Long> {
    @Query(
            "SELECT f "
                    + "From FriendEntity f "
                    + "WHERE f.member.id = :memberId "
                    + "AND f.following.id = :followingId")
    Optional<FriendEntity> findByMemberAndFollowing(
            @Param("memberId") Long memberId, @Param("followingId") Long followingId);
}
