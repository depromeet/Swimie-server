package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberDetailResponse(
        @Schema(description = "멤버 id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long id,
        @Schema(description = "닉네임", example = "김스위미", requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(description = "목표", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
                Integer goal,
        @Schema(description = "프로필 이미지 URL", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String profileImageUrl) {
    public static MemberDetailResponse of(Member member, String profileImageOrigin) {
        return new MemberDetailResponse(
                member.getId(),
                member.getNickname(),
                member.getGoal(),
                getProfileImageUrl(profileImageOrigin, member.getProfileImageUrl()));
    }

    private static String getProfileImageUrl(String profileImageOrigin, String profileImageUrl) {
        if (profileImageUrl == null) {
            return null;
        }
        return profileImageOrigin + "/" + profileImageUrl;
    }
}
