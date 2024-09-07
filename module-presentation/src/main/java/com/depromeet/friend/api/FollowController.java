package com.depromeet.friend.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.friend.dto.request.FollowRequest;
import com.depromeet.friend.dto.response.*;
import com.depromeet.friend.facade.FollowFacade;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.friend.FollowSuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FollowController implements FollowApi {
    private final FollowFacade followFacade;

    @PutMapping
    @Logging(item = "Follower/Following", action = "PUT")
    public ApiResponse<?> addOrDeleteFollow(
            @LoginMember Long memberId, @RequestBody FollowRequest followRequest) {
        boolean hasFollowAdded = followFacade.addOrDeleteFollow(memberId, followRequest);

        if (hasFollowAdded) {
            return ApiResponse.success(FollowSuccessType.ADD_FOLLOWING_SUCCESS);
        }
        return ApiResponse.success(FollowSuccessType.DELETE_FOLLOWING_SUCCESS);
    }

    @GetMapping("/{memberId}/following")
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowSliceResponse<FollowingResponse>> findFollowingList(
            @LoginMember Long requesterId,
            @PathVariable(value = "memberId") Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        FollowSliceResponse<FollowingResponse> response =
                followFacade.findFollowingList(memberId, requesterId, cursorId);
        return ApiResponse.success(FollowSuccessType.GET_FOLLOWINGS_SUCCESS, response);
    }

    @GetMapping("/{memberId}/follower")
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowSliceResponse<FollowerResponse>> findFollowerList(
            @LoginMember Long requesterId,
            @PathVariable(value = "memberId") Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        FollowSliceResponse<FollowerResponse> response =
                followFacade.findFollowerList(memberId, requesterId, cursorId);
        return ApiResponse.success(FollowSuccessType.GET_FOLLOWERS_SUCCESS, response);
    }

    @GetMapping("/following/summary")
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowingSummaryResponse> findFollowingSummary(@LoginMember Long memberId) {
        FollowingSummaryResponse response = followFacade.findFollowingSummary(memberId);
        return ApiResponse.success(FollowSuccessType.GET_FOLLOWING_SUMMARY_SUCCESS, response);
    }

    @GetMapping
    @Logging(item = "Follower/Following", action = "GET")
    public ApiResponse<FollowingStateResponse> checkFollowingState(
            @LoginMember Long memberId, @RequestParam("ids") List<Long> ids) {
        FollowingStateResponse response = followFacade.checkFollowingState(memberId, ids);
        return ApiResponse.success(FollowSuccessType.CHECK_FOLLOWING_SUCCESS, response);
    }
}
