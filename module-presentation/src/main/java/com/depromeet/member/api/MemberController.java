package com.depromeet.member.api;

import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.domain.Member;
import com.depromeet.member.dto.request.GenderUpdateRequest;
import com.depromeet.member.dto.request.NicknameUpdateRequest;
import com.depromeet.member.dto.response.MemberFindOneResponse;
import com.depromeet.member.dto.response.MemberGenderResponse;
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
    public ApiResponse<MemberFindOneResponse> getMember(@PathVariable("id") Long id) {
        Member member = memberFacade.findById(id);
        return ApiResponse.success(MemberSuccessType.GET_SUCCESS, MemberFindOneResponse.of(member));
    }

    @PatchMapping("/nickname")
    @Logging(item = "Member", action = "PATCH")
    public ApiResponse<MemberFindOneResponse> updateNickname(
            @LoginMember Long memberId, @Valid @RequestBody NicknameUpdateRequest updateNicknameRequest) {
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
}
