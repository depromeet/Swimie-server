package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.member.dto.request.NameUpdateRequest;
import com.depromeet.member.dto.response.MemberFindOneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사용자(members)")
public interface MemberApi {
    @Operation(summary = "id로 member 단일 검색")
    ApiResponse<MemberFindOneResponse> getMember(@PathVariable("id") Long id);

    @Operation(summary = "닉네임 수정")
    ApiResponse<MemberFindOneResponse> updateName(
            @LoginMember Long id, @Valid @RequestBody NameUpdateRequest updateNameRequest);
}
