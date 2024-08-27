package com.depromeet.withdrawal.service;

import com.depromeet.withdrawal.domain.ReasonType;
import com.depromeet.withdrawal.port.in.command.CreateWithdrawalReasonCommand;
import com.depromeet.withdrawal.port.in.usecase.CreateWithdrawalReasonUseCase;
import com.depromeet.withdrawal.port.out.persistence.WithdrawalReasonPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawalReasonService implements CreateWithdrawalReasonUseCase {
    private final WithdrawalReasonPort withdrawalReasonPort;

    @Override
    public void save(CreateWithdrawalReasonCommand withdrawalReasonCommand) {
        ReasonType reasonType = ReasonType.findByCode(withdrawalReasonCommand.reasonCode());
        String feedback = withdrawalReasonCommand.feedback();
        withdrawalReasonPort.writeToSheet(reasonType, feedback);
    }
}
