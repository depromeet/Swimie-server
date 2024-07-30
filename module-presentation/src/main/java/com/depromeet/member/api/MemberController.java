package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.domain.Member;
import com.depromeet.member.dto.request.NameUpdateRequest;
import com.depromeet.member.dto.response.MemberFindOneResponse;
import com.depromeet.member.facade.MemberFacade;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.type.member.MemberSuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberApi {
    private final MemberFacade memberFacade;

    @GetMapping("/{id}")
    public ApiResponse<MemberFindOneResponse> getMember(@PathVariable("id") Long id) {
        Member member = memberFacade.findById(id);
        return ApiResponse.success(MemberSuccessType.GET_SUCCESS, MemberFindOneResponse.of(member));
    }

    @PatchMapping
    public ApiResponse<MemberFindOneResponse> updateName(
            @LoginMember Long id,
            @RequestBody NameUpdateRequest updateNameRequest) {
        Member member = memberFacade.updateName(id, updateNameRequest.name());
        return ApiResponse.success(MemberSuccessType.UPDATE_NAME_SUCCESS, MemberFindOneResponse.of(member));
    }
}
