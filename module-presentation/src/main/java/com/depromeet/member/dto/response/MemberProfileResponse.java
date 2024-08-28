package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberProfileResponse(
        @Schema(description = "멤버 id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(description = "닉네임", example = "김스위미", requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "한 줄 소개",
                        example = "만나서 반갑습니다",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String introduction,
        @Schema(
                        description = "프로필 이미지 URL",
                        example = "https://cloud.front.domain/image-name.png",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String profileImageUrl,
        @Schema(description = "팔로워 수", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
                Integer followerCount,
        @Schema(description = "팔로잉 수", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
                Integer followingCount,
        @Schema(
                        description = "로그인한 멤버의 정보인지",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Boolean isMyProfile) {
    public static MemberProfileResponse of(
            Member member,
            String profileImageOrigin,
            Integer followerCount,
            Integer followingCount,
            Boolean isMyProfile) {
        return new MemberProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getIntroduction(),
                member.getProfileImageUrl(profileImageOrigin),
                followerCount,
                followingCount,
                isMyProfile);
    }
}
