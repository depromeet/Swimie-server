package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.dto.request.GoalUpdateRequest;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "목표(goal)")
public interface GoalApi {
    @Operation(summary = "목표 수정")
    ApiResponse<MemberSimpleResponse> update(
            @LoginMember Long memberId, @RequestBody GoalUpdateRequest goalUpdateRequest);

    @Operation(summary = "목표 조회")
    ApiResponse<MemberSimpleResponse> findGoal(@PathVariable("memberId") Long memberId);
}
