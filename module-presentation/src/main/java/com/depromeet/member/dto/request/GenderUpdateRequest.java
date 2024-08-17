package com.depromeet.member.dto.request;

import com.depromeet.annotation.Enum;
import com.depromeet.member.domain.MemberGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GenderUpdateRequest(
        @NotBlank
        @Enum(enumClass = MemberGender.class, ignoreCase = true)
        @Schema(description = "성별", example = "M", requiredMode = Schema.RequiredMode.REQUIRED)
        String gender) {}
