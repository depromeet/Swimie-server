package com.depromeet.withdrawalReason.repository;

import com.depromeet.withdrawalReason.entity.WithdrawalReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalReasonJpaRepository
        extends JpaRepository<WithdrawalReasonEntity, Long> {}
