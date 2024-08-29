package com.depromeet.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AppleLoginRequest(
        @Schema(description = "회원 정보", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                AppleAccountInfo user,
        @NotNull(message = "인가 코드는 null일 수 없습니다")
                @Schema(description = "인가 코드", requiredMode = Schema.RequiredMode.REQUIRED)
                String code,
        @NotNull(message = "ID 토큰은 null일 수 없습니다")
                @Schema(description = "ID 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
                String idToken) {}
