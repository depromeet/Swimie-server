package com.depromeet.report.dto.request;

import com.depromeet.report.domain.ReportReasonCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(
        @NotNull
                @Schema(
                        description = "신고할 기록 id",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memoryId,
        @NotNull
                @Schema(
                        description = "신고 사유 code",
                        example = "REPORT_REASON_1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                ReportReasonCode reasonCode) {}
