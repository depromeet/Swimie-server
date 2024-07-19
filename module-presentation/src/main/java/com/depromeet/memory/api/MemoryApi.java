package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.security.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest);

    @Operation(summary = "수영 기록 단일 조회")
    ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId);

    @Operation(summary = "타임라인 최신순 조회 및 달력 조회 후, 위/아래 무한 스크롤 구현")
    ApiResponse<?> timelineCalendar(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "cursorRecordAt", required = false) String cursorRecordAt,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "showNewer", required = false) boolean showNewer,
            @RequestParam(value = "size") Integer size);
}
