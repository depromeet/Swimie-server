package com.depromeet.member.api;

import com.depromeet.member.dto.response.MemberFindOneResponseDto;
import com.depromeet.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController implements MemberApi {
    private final MemberService memberService;

    @GetMapping("/{id}")
    public MemberFindOneResponseDto getMember(@PathVariable Long id) {
        return memberService.findOneMemberResponseById(id);
    }
}
