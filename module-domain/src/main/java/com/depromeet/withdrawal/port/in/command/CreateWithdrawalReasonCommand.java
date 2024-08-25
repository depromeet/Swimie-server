package com.depromeet.withdrawal.port.in.command;

import lombok.Builder;

public record CreateWithdrawalReasonCommand(String reason, String feedback) {
    @Builder
    public CreateWithdrawalReasonCommand {}
}
