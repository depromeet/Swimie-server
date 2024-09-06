package com.depromeet.blacklist.api;

import com.depromeet.blacklist.dto.request.BlackMemberRequest;
import com.depromeet.blacklist.dto.response.BlackMemberResponse;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "차단(Blacklist)")
public interface BlacklistApi {
    @Operation(summary = "사용자 차단")
    ApiResponse<?> blackMember(
            @LoginMember Long memberId, @Valid @RequestBody BlackMemberRequest request);

    @Operation(summary = "사용자 차단 해제")
    ApiResponse<?> unblackMember(
            @LoginMember Long memberId, @PathVariable("blackMemberId") Long blackMemberId);

    @Operation(summary = "사용자 차단 목록 조회")
    ApiResponse<BlackMemberResponse> getBlackMembers(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId);
}
