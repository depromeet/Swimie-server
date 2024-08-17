package com.depromeet.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
        @NotBlank(message = "멤버의 이름은 비어 있을 수 없습니다")
        @Schema(description = "닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
        String nickname,
        @Schema(description = "한 줄 소개", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String introduction) {
    public MemberUpdateRequest {}
}
