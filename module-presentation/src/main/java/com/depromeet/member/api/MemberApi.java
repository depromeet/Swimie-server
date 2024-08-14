package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.dto.request.GenderUpdateRequest;
import com.depromeet.member.dto.request.NicknameUpdateRequest;
import com.depromeet.member.dto.response.MemberFindOneResponse;
import com.depromeet.member.dto.response.MemberGenderResponse;
import com.depromeet.member.dto.response.MemberProfileResponse;
import com.depromeet.member.dto.response.MemberSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "사용자(members)")
public interface MemberApi {
    @Operation(summary = "id로 member 단일 검색")
    ApiResponse<MemberProfileResponse> getMember(
            @LoginMember Long memberId, @PathVariable("id") Long id);

    @Operation(summary = "닉네임 수정")
    ApiResponse<MemberFindOneResponse> updateNickname(
            @LoginMember Long memberId,
            @Valid @RequestBody NicknameUpdateRequest updateNameRequest);

    @Operation(summary = "성별 수정")
    ApiResponse<MemberGenderResponse> updateGender(
            @LoginMember Long memberId,
            @Valid @RequestBody GenderUpdateRequest genderUpdateRequest);

    @Operation(summary = "사용자 검색")
    ApiResponse<MemberSearchResponse> searchMember(
            @RequestParam(name = "nameQuery", required = false) String nameQuery,
            @RequestParam(name = "cursorId", required = false) Long cursorId);
}
