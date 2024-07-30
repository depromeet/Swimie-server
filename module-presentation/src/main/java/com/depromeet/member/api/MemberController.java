package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.domain.Member;
import com.depromeet.member.dto.response.MemberFindOneResponse;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.type.member.MemberSuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController implements MemberApi {
    private final MemberUseCase memberUseCase;

    @GetMapping("/{id}")
    public ApiResponse<MemberFindOneResponse> getMember(@PathVariable("id") Long id) {
        Member member = memberUseCase.findById(id);
        return ApiResponse.success(MemberSuccessType.GET_SUCCESS, MemberFindOneResponse.of(member));
    }
}
