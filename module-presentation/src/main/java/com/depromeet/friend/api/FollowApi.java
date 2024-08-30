package com.depromeet.friend.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.*;
import com.depromeet.member.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "팔로워/팔로잉(Follower/Following)")
public interface FollowApi {
    @Operation(summary = "팔로잉 추가")
    ApiResponse<?> addOrDeleteFollow(
            @LoginMember Long memberId, @RequestBody FollowRequest followRequest);

    @Operation(summary = "팔로잉 리스트 조회")
    ApiResponse<FollowSliceResponse<FollowingResponse>> findFollowingList(
            @Parameter(description = "팔로잉 리스트 조회할 대상의 member PK") @PathVariable(value = "memberId")
                    Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId);

    @Operation(summary = "팔로워 리스트 조회")
    ApiResponse<FollowSliceResponse<FollowerResponse>> findFollowerList(
            @Parameter(description = "팔로워 리스트 조회할 대상의 member PK") @PathVariable(value = "memberId")
                    Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId);

    @Operation(summary = "팔로잉 소식 페이지에 사용할 팔로잉 유저 목록 조회")
    ApiResponse<FollowingSummaryResponse> findFollowingSummary(@LoginMember Long memberId);

    @Operation(summary = "팔로잉 여부 조회")
    ApiResponse<FollowingStateResponse> checkFollowingState(
            @LoginMember Long memberId,
            @Parameter(description = "팔로잉 여부를 조회할 대상들의 PK 리스트", example = "1,2,3,4,5")
                    @RequestParam("ids")
                    List<Long> ids);
}
