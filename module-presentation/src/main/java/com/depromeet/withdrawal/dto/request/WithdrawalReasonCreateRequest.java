package com.depromeet.withdrawal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record WithdrawalReasonCreateRequest(
        @NotNull
                @Schema(
                        description = "탈퇴 사유",
                        example = "REASON_NOT_SWIM",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String reason,
        @Schema(description = "피드백", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String feedback) {}
