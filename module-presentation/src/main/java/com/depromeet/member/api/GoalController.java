package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.Member;
import com.depromeet.member.dto.request.GoalUpdateRequest;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.member.service.MemberService;
import com.depromeet.security.LoginMember;
import com.depromeet.type.member.MemberSuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController implements GoalApi {
    private final MemberService memberService;

    @PatchMapping
    public ApiResponse<MemberSimpleResponse> update(
            @LoginMember Long memberId,
            @RequestBody GoalUpdateRequest goalUpdateRequest) {
        Member member = memberService.updateGoal(memberId, goalUpdateRequest.goal());
        return ApiResponse.success(
                MemberSuccessType.UPDATE_GOAL_SUCCESS,
                new MemberSimpleResponse(member.getGoal(), member.getName()));
    }

    @GetMapping("/{memberId}")
    public ApiResponse<MemberSimpleResponse> findGoal(@PathVariable("memberId") Long memberId) {
        Member member = memberService.findById(memberId);
        return ApiResponse.success(
                MemberSuccessType.GET_GOAL_SUCCESS,
                new MemberSimpleResponse(member.getGoal(), member.getName()));
    }
}
