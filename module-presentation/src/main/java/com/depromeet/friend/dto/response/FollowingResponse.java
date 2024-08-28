package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.Following;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowingResponse(
        @Schema(
                        description = "팔로잉 member PK",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(
                        description = "팔로잉 member 이름",
                        example = "홍길동",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "팔로잉 member 프로필 사진 url",
                        example = "https://image.webp",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String profileImageUrl,
        @Schema(
                        description = "팔로잉 member 한 줄 소개",
                        example = "안녕하세요. 홍길동입니다",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String introduction) {
    @Builder
    public FollowingResponse {}

    public static FollowingResponse toFollowingResponse(
            Following following, String profileImageOrigin) {
        return FollowingResponse.builder()
                .memberId(following.getMemberId())
                .nickname(following.getName())
                .profileImageUrl(
                        getProfileImageUrl(profileImageOrigin, following.getProfileImageUrl()))
                .introduction(following.getIntroduction())
                .build();
    }

    private static String getProfileImageUrl(String profileImageOrigin, String profileImageUrl) {
        if (profileImageUrl != null) {
            return profileImageOrigin + "/" + profileImageUrl;
        }
        return null;
    }
}
