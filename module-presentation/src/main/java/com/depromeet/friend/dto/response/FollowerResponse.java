package com.depromeet.friend.dto.response;

import com.depromeet.friend.domain.vo.Follower;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowerResponse(
        @Schema(
                        description = "팔로워 member PK",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(
                        description = "팔로워 member nickname",
                        example = "홍길동",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "팔로워 프로필 사진 url",
                        example = "https://image.webp",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String profileImageUrl,
        @Schema(
                        description = "팔로잉 member PK",
                        example = "안녕하세요. 홍길동입니다",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String introduction) {
    @Builder
    public FollowerResponse {}

    public static FollowerResponse of(Follower follower, String profileImageOrigin) {
        return FollowerResponse.builder()
                .memberId(follower.getMemberId())
                .nickname(follower.getName())
                .profileImageUrl(follower.getProfileImageUrl(profileImageOrigin))
                .introduction(follower.getIntroduction())
                .build();
    }
}
