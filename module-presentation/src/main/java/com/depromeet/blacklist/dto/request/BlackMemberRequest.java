package com.depromeet.blacklist.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BlackMemberRequest(
        @NotNull
                @Positive
                @Schema(
                        description = "차단 대상 사용자 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long blackMemberId) {}
