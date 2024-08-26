package com.depromeet.withdrawal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record WithdrawalReasonCreateRequest(
        @NotNull
                @Schema(
                        description = "탈퇴 사유",
                        example = "REASON_01",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String reasonCode,
        @Schema(description = "피드백", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String feedback) {}
