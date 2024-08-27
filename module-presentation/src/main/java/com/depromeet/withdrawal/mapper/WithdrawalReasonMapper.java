package com.depromeet.withdrawal.mapper;

import com.depromeet.withdrawal.dto.request.WithdrawalReasonCreateRequest;
import com.depromeet.withdrawal.port.in.command.CreateWithdrawalReasonCommand;

public class WithdrawalReasonMapper {
    public static CreateWithdrawalReasonCommand toCommand(WithdrawalReasonCreateRequest request) {
        return CreateWithdrawalReasonCommand.builder()
                .reasonCode(request.reasonCode())
                .feedback(request.feedback())
                .build();
    }
}
