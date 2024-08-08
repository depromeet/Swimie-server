package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.CalendarResponse;
import com.depromeet.memory.dto.response.MemoryCreateResponse;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.dto.response.MemoryUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<MemoryCreateResponse> create(
            @LoginMember Long memberId,
            @Valid @RequestBody MemoryCreateRequest memoryCreateRequest);

    @Operation(summary = "수영 기록 수정을 위한 단일 조회")
    ApiResponse<MemoryUpdateResponse> readForUpdate(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId);

    @Operation(summary = "수영 기록 단일 조회")
    ApiResponse<MemoryResponse> read(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId);

    @Operation(summary = "수영 기록 수정")
    ApiResponse<MemoryResponse> update(
            @LoginMember Long memberId,
            @PathVariable("memoryId") Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest memoryUpdateRequest);

    @Operation(summary = "타임라인 최신순 조회")
    ApiResponse<?> timeline(
            @LoginMember Long memberId,
            @Parameter(description = "커서 기준 (recordAt)", example = "2024-07-31")
                    @RequestParam(name = "cursorRecordAt", required = false)
                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate cursorRecordAt);

    @Operation(summary = "캘린더 조회")
    ApiResponse<CalendarResponse> getCalendar(
            @LoginMember Long memberId,
            @RequestParam("year") Integer year,
            @RequestParam("month") Short month);
}
