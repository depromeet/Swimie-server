package com.depromeet.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record NicknameUpdateRequest(
        @NotBlank
        @Schema(description = "닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
        String nickname) {}
