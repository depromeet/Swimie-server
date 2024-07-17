package com.depromeet.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record GoogleLoginRequest(
        @NotNull(message = "인가 코드는 null일 수 없습니다")
        String code
) {
}
