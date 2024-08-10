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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FollowController implements FollowApi {
    private final FollowFacade followFacade;

    @PostMapping
    public ApiResponse<?> addOrDeleteFollowing(
            @LoginMember Long memberId, FollowingRequest followingRequest) {
        followFacade.addFollowing(memberId, followingRequest);

        return ApiResponse.success(FollowSuccessType.ADD_FOLLOWING_SUCCESS);
    }

    @GetMapping("/following")
    public ApiResponse<FollowSliceResponse<FollowingResponse>> findFollowingList(
            @LoginMember Long memberId, @RequestParam("cursorId") Long cursorId) {
        FollowSliceResponse<FollowingResponse> response =
                followFacade.findFollowingList(memberId, cursorId);

        return ApiResponse.success(FollowSuccessType.GET_FOLLOWINGS_SUCCESS, response);
    }

    @GetMapping("/follower")
    public ApiResponse<FollowSliceResponse<FollowerResponse>> findFollowerList(
            @LoginMember Long memberId) {
        return null;
    }

    @GetMapping("/count")
    public ApiResponse<FollowerFollowingCountResponse> getFollowerFollowingCount(
            @LoginMember Long memberId) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> cancelFollowing(@LoginMember Long memberId, Long followingId) {
        return null;
    }
}
