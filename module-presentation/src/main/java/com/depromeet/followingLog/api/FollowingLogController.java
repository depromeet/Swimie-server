package com.depromeet.followingLog.api;

import static com.depromeet.type.followingLog.FollowingLogSuccessType.GET_FOLLOWING_LOGS_SUCCESS;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.followingLog.dto.response.FollowingLogSliceResponse;
import com.depromeet.followingLog.facade.FollowingLogFacade;
import com.depromeet.member.annotation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class FollowingLogController implements FollowingLogApi {
    private final FollowingLogFacade followingLogFacade;

    @GetMapping
    @Logging(item = "FollowingLog", action = "GET")
    public ApiResponse<FollowingLogSliceResponse> getFollowingLogs(
            @LoginMember Long memberId,
            @RequestParam(name = "cursorId", required = false) Long cursorId) {
        FollowingLogSliceResponse response =
                followingLogFacade.getLogsByMemberIdAndCursorId(memberId, cursorId);
        return ApiResponse.success(GET_FOLLOWING_LOGS_SUCCESS, response);
    }
}
