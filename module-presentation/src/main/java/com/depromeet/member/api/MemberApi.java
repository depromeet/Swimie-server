package com.depromeet.member.api;

import com.depromeet.member.dto.response.MemberFindOneResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자(members)")
public interface MemberApi {
    @Operation(summary = "id로 member 단일 검색")
    MemberFindOneResponseDto getMember(@PathVariable Long id);
}
