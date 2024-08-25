package com.depromeet.withdrawal.port.out.persistence;

import com.depromeet.withdrawal.domain.WithdrawalReason;

public interface WithdrawalReasonPort {
    void save(WithdrawalReason withdrawalReason);
}
