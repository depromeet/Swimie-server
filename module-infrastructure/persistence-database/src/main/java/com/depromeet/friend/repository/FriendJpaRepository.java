package com.depromeet.friend.repository;

import com.depromeet.friend.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendJpaRepository extends JpaRepository<FriendEntity, Long> {}
