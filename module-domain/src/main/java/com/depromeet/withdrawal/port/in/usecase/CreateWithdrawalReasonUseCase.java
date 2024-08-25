package com.depromeet.withdrawal.port.in.usecase;

import com.depromeet.withdrawal.port.in.command.CreateWithdrawalReasonCommand;

public interface CreateWithdrawalReasonUseCase {
    void save(CreateWithdrawalReasonCommand createWithdrawalReasonCommand);
}
