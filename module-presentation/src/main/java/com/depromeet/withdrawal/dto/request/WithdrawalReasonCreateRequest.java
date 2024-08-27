package com.depromeet.withdrawal.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WithdrawalReasonCreateRequest(
        @NotNull
                @Schema(
                        description = "탈퇴 사유",
                        example = "REASON_01",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String reasonCode,
        @Size(max = 1000, message = "1000자 이내로만 작성 가능합니다")
                @Schema(description = "피드백", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String feedback) {}
