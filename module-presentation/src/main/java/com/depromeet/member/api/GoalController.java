package com.depromeet.member.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.domain.Member;
import com.depromeet.member.dto.request.GoalUpdateRequest;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.member.port.in.usecase.GoalUpdateUseCase;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.type.member.MemberSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController implements GoalApi {
    private final MemberUseCase memberUseCase;
    private final GoalUpdateUseCase goalUpdateUseCase;

    @PatchMapping
    @Logging(item = "Goal", action = "PATCH")
    public ApiResponse<MemberSimpleResponse> update(
            @LoginMember Long memberId, @Valid @RequestBody GoalUpdateRequest goalUpdateRequest) {
        Member member = goalUpdateUseCase.updateGoal(memberId, goalUpdateRequest.goal());
        return ApiResponse.success(
                MemberSuccessType.UPDATE_GOAL_SUCCESS,
                new MemberSimpleResponse(member.getGoal(), member.getNickname()));
    }

    @GetMapping("/{memberId}")
    @Logging(item = "Goal", action = "GET")
    public ApiResponse<MemberSimpleResponse> findGoal(@PathVariable("memberId") Long memberId) {
        Member member = memberUseCase.findById(memberId);
        return ApiResponse.success(
                MemberSuccessType.GET_GOAL_SUCCESS,
                new MemberSimpleResponse(member.getGoal(), member.getNickname()));
    }
}
