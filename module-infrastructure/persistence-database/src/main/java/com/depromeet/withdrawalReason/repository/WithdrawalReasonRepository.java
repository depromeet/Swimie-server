package com.depromeet.withdrawalReason.repository;

import com.depromeet.withdrawal.domain.WithdrawalReason;
import com.depromeet.withdrawal.port.out.persistence.WithdrawalReasonPort;
import com.depromeet.withdrawalReason.entity.WithdrawalReasonEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WithdrawalReasonRepository implements WithdrawalReasonPort {
    private final WithdrawalReasonJpaRepository withdrawalReasonJpaRepository;

    public void save(WithdrawalReason withdrawalReason) {
        withdrawalReasonJpaRepository.save(WithdrawalReasonEntity.from(withdrawalReason));
    }
}
