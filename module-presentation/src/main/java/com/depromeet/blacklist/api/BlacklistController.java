package com.depromeet.blacklist.api;

import com.depromeet.blacklist.dto.request.BlackMemberRequest;
import com.depromeet.blacklist.facade.BlacklistFacade;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.blacklist.BlacklistSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member/black")
public class BlacklistController implements BlacklistApi {
    private final BlacklistFacade blacklistFacade;

    @PostMapping
    public ApiResponse<?> blackMember(
            @LoginMember Long memberId, @Valid @RequestBody BlackMemberRequest request) {
        blacklistFacade.blackMember(memberId, request);
        return ApiResponse.success(BlacklistSuccessType.BLACK_MEMBER_SUCCESS);
    }
}
