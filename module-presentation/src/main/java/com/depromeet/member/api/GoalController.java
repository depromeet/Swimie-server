package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.Member;
import com.depromeet.member.dto.request.GoalUpdateRequest;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.member.facade.GoalFacade;
import com.depromeet.member.service.GoalService;
import com.depromeet.member.service.MemberService;
import com.depromeet.type.member.MemberSuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController implements GoalApi {
    private final GoalFacade goalFacade;

    @PatchMapping("/{memberId}")
    public ApiResponse<MemberSimpleResponse> update(
            @PathVariable("memberId") Long memberId,
            @RequestBody GoalUpdateRequest goalUpdateRequest) {
        return ApiResponse.success(
                MemberSuccessType.UPDATE_GOAL_SUCCESS,
                goalFacade.update(memberId, goalUpdateRequest.goal()));
    }

    @GetMapping("/{memberId}")
    public ApiResponse<MemberSimpleResponse> findGoal(Long memberId) {
        return ApiResponse.success(MemberSuccessType.GET_GOAL_SUCCESS, goalFacade.findGoalById(memberId));
    }
}
