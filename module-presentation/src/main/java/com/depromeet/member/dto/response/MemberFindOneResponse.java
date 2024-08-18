package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberFindOneResponse(
        @Schema(description = "멤버 id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long id,
        @Schema(description = "닉네임", example = "김스위미", requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "이메일",
                        example = "swimie@gmail.com",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String email) {
    @Builder
    public MemberFindOneResponse {}

    public static MemberFindOneResponse of(Member member) {
        return new MemberFindOneResponse(member.getId(), member.getNickname(), member.getEmail());
    }
}
