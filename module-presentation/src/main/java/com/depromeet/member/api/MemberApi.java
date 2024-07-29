package com.depromeet.member.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.dto.response.MemberFindOneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자(members)")
public interface MemberApi {
    @Operation(summary = "id로 member 단일 검색")
    ApiResponse<MemberFindOneResponse> getMember(@PathVariable("id") Long id);
}
