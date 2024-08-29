package com.depromeet.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AppleAccountInfo(
        @Schema(description = "성명", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                AppleNameInfo name,
        @Schema(description = "이메일", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String email) {}
