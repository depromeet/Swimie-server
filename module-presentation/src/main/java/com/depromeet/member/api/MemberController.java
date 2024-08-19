package com.depromeet.member.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.domain.Member;
import com.depromeet.member.dto.request.GenderUpdateRequest;
import com.depromeet.member.dto.request.MemberUpdateRequest;
import com.depromeet.member.dto.request.NicknameUpdateRequest;
import com.depromeet.member.dto.response.*;
import com.depromeet.member.facade.MemberFacade;
import com.depromeet.type.member.MemberSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberApi {
    private final MemberFacade memberFacade;

    @GetMapping("/{id}")
    @Logging(item = "Member", action = "GET")
    public ApiResponse<MemberProfileResponse> getMember(
            @LoginMember Long memberId, @PathVariable("id") Long id) {
        return ApiResponse.success(
                MemberSuccessType.GET_SUCCESS, memberFacade.findById(memberId, id));
    }

    @PatchMapping
    public ApiResponse<MemberUpdateResponse> updateMember(
            @LoginMember Long memberId,
            @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return ApiResponse.success(
                MemberSuccessType.UPDATE_SUCCESS,
                memberFacade.update(memberId, memberUpdateRequest));
    }

    @PatchMapping("/nickname")
    @Logging(item = "Member", action = "PATCH")
    public ApiResponse<MemberFindOneResponse> updateNickname(
            @LoginMember Long memberId,
            @Valid @RequestBody NicknameUpdateRequest updateNicknameRequest) {
        Member member = memberFacade.updateNickname(memberId, updateNicknameRequest.nickname());
        return ApiResponse.success(
                MemberSuccessType.UPDATE_NAME_SUCCESS, MemberFindOneResponse.of(member));
    }

    @PatchMapping("/gender")
    @Logging(item = "Member", action = "PATCH")
    public ApiResponse<MemberGenderResponse> updateGender(
            @LoginMember Long memberId,
            @Valid @RequestBody GenderUpdateRequest genderUpdateRequest) {
        Member member = memberFacade.updateGender(memberId, genderUpdateRequest.gender());
        return ApiResponse.success(
                MemberSuccessType.UPDATE_GENDER_SUCCESS, MemberGenderResponse.of(member));
    }

    @GetMapping("/search")
    @Logging(item = "Member", action = "GET")
    public ApiResponse<MemberSearchResponse> searchMember(
            @LoginMember Long memberId,
            @RequestParam(name = "nameQuery", required = false) String nameQuery,
            @RequestParam(name = "cursorId", required = false) Long cursorId) {
        MemberSearchResponse response = memberFacade.searchByName(memberId, nameQuery, cursorId);
        return ApiResponse.success(MemberSuccessType.SEARCH_MEMBER_SUCCESS, response);
    }

    @GetMapping
    public ApiResponse<MemberDetailResponse> getDetail(@LoginMember Long memberId) {
        return ApiResponse.success(
                MemberSuccessType.GET_DETAIL_SUCCESS, memberFacade.findDetailById(memberId));
    }
}
