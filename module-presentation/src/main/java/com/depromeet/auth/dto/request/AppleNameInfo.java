package com.depromeet.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AppleNameInfo(
        @Schema(description = "이름", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String firstName,
        @Schema(description = "성", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String lastName) {}
