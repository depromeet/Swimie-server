package com.depromeet.withdrawal.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WithdrawalReason {
    private Long id;
    private ReasonType reason;
    private String feedback;
    private LocalDateTime createdAt;

    @Builder
    public WithdrawalReason(Long id, ReasonType reason, String feedback, LocalDateTime createdAt) {
        this.id = id;
        this.reason = reason;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }
}
