package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberGenderResponse(
        @Schema(description = "멤버 id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long id,
        @Schema(description = "목표", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
                Integer goal,
        @Schema(description = "닉네임", example = "김스위미", requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "이메일",
                        example = "king2dwellsang@gmail.com",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String email,
        @Schema(description = "성별", example = "W", requiredMode = Schema.RequiredMode.REQUIRED)
                MemberGender gender) {
    public static MemberGenderResponse of(Member member) {
        return new MemberGenderResponse(
                member.getId(),
                member.getGoal(),
                member.getNickname(),
                member.getEmail(),
                member.getGender());
    }
}
