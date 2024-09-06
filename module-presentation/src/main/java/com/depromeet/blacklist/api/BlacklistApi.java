package com.depromeet.blacklist.api;

import com.depromeet.blacklist.dto.request.BlackMemberRequest;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "차단(Blacklist)")
public interface BlacklistApi {
    @Operation(summary = "사용자 차단")
    ApiResponse<?> blackMember(
            @LoginMember Long memberId, @Valid @RequestBody BlackMemberRequest request);
}
