package com.depromeet.memory.controller;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.security.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest);

    @Operation(summary = "최신 기록부터 내림차순 타임라인 조회")
    ApiResponse<?> getTimeline(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size") int size,
            @LoginMember Long memberId);
}
