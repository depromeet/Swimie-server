package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberUpdateResponse(
        @Schema(description = "멤버 id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long memberId,
        @Schema(description = "닉네임", example = "김스위미", requiredMode = Schema.RequiredMode.REQUIRED)
        String nickname,
        @Schema(description = "한 줄 소개", example = "만나서 반갑습니다", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String introduction) {
    public static MemberUpdateResponse of(Member member) {
        return new MemberUpdateResponse(
                member.getId(), member.getNickname(), member.getIntroduction());
    }
}
