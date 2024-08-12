package com.depromeet.friend.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.friend.dto.request.FollowingRequest;
import com.depromeet.friend.dto.response.FollowSliceResponse;
import com.depromeet.friend.dto.response.FollowerFollowingCountResponse;
import com.depromeet.friend.dto.response.FollowerResponse;
import com.depromeet.friend.dto.response.FollowingResponse;
import com.depromeet.friend.facade.FollowFacade;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.friend.FollowSuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FollowController implements FollowApi {
    private final FollowFacade followFacade;

    @PostMapping
    public ApiResponse<?> addOrDeleteFollowing(
            @LoginMember Long memberId, FollowingRequest followingRequest) {
        boolean hasFollowingAdded = followFacade.addFollowing(memberId, followingRequest);

        if (hasFollowingAdded) {
            return ApiResponse.success(FollowSuccessType.ADD_FOLLOWING_SUCCESS);
        }

        return ApiResponse.success(FollowSuccessType.DELETE_FOLLOWING_SUCCESS);
    }

    @GetMapping("/following")
    public ApiResponse<FollowSliceResponse<FollowingResponse>> findFollowingList(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        FollowSliceResponse<FollowingResponse> response =
                followFacade.findFollowingList(memberId, cursorId);

        return ApiResponse.success(FollowSuccessType.GET_FOLLOWINGS_SUCCESS, response);
    }

    @GetMapping("/follower")
    public ApiResponse<FollowSliceResponse<FollowerResponse>> findFollowerList(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        FollowSliceResponse<FollowerResponse> response =
                followFacade.findFollowerList(memberId, cursorId);

        return ApiResponse.success(FollowSuccessType.GET_FOLLOWERS_SUCCESS, response);
    }

    @GetMapping("/count")
    public ApiResponse<FollowerFollowingCountResponse> getFollowerFollowingCount(
            @LoginMember Long memberId) {
        FollowerFollowingCountResponse response = followFacade.countFollowerAndFollowing(memberId);

        return ApiResponse.success(
                FollowSuccessType.GET_FOLLOWER_FOLLOWING_COUNT_SUCCESS, response);
    }
}
