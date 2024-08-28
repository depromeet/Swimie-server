package com.depromeet.reaction.dto.response;

import com.depromeet.reaction.domain.Reaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReactionDetailResponse(
        @Schema(description = "응원 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long reactionId,
        @Schema(description = "이모지", example = "🦭", requiredMode = Schema.RequiredMode.REQUIRED)
                String emoji,
        @Schema(
                        description = "코멘트",
                        example = "물개세요?",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String comment,
        @Schema(
                        description = "응원을 한 멤버 ID",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(description = "닉네임", example = "스위미", requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "프로필 이미지 URL",
                        example = "https://url.com",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String profileImageUrl) {
    public static ReactionDetailResponse from(Reaction reaction, String profileImageOrigin) {
        return new ReactionDetailResponse(
                reaction.getId(),
                reaction.getEmoji(),
                reaction.getComment(),
                reaction.getMember().getId(),
                reaction.getMember().getNickname(),
                reaction.getMember().getProfileImageUrl(profileImageOrigin));
    }
}
