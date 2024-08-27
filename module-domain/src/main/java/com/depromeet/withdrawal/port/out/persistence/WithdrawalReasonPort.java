package com.depromeet.withdrawal.port.out.persistence;

import com.depromeet.withdrawal.domain.ReasonType;

public interface WithdrawalReasonPort {
    void writeToSheet(ReasonType reasonType, String feedback);
}
