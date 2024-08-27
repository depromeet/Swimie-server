package com.depromeet.followinglog.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.followinglog.dto.response.FollowingLogSliceResponse;
import com.depromeet.member.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "팔로잉 소식 (Following News)")
public interface FollowingLogApi {
    @Operation(summary = "팔로우 소식 조회")
    ApiResponse<FollowingLogSliceResponse> getFollowingLogs(
            @LoginMember Long memberId,
            @RequestParam(name = "cursorId", required = false) Long cursorId);
}
