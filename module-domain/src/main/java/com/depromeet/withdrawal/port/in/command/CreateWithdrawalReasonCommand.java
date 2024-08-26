package com.depromeet.withdrawal.port.in.command;

import lombok.Builder;

public record CreateWithdrawalReasonCommand(String reasonCode, String feedback) {
    @Builder
    public CreateWithdrawalReasonCommand {}
}
