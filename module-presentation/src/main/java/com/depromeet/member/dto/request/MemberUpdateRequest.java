package com.depromeet.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateRequest(
        @Schema(description = "닉네임", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String nickname,
        @Schema(description = "한 줄 소개", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String introduction) {
    public MemberUpdateRequest {}
}
