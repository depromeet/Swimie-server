package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequestDto;
import com.depromeet.memory.dto.response.CalendarResponse;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.security.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.time.YearMonth;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<?> create(
            @LoginMember Long memberId,
            @Valid @RequestBody MemoryCreateRequest memoryCreateRequest);

    @Operation(summary = "수영 기록 단일 조회")
    ApiResponse<MemoryResponse> read(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId);

    @Operation(summary = "수영 기록 수정")
    ApiResponse<MemoryResponse> update(
            @LoginMember Long memberId,
            @PathVariable("memoryId") Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest memoryUpdateRequest);

    @Operation(summary = "타임라인 최신순 조회 및 달력 조회 후, 위/아래 무한 스크롤 구현")
    ApiResponse<?> timelineCalendar(
            @LoginMember Long memberId, @ModelAttribute TimelineRequestDto timelineRequestDto);

    @Operation(summary = "타임라인 최신순 조회")
    ApiResponse<?> timeline(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "recordAt", required = false) String recordAt,
            @RequestParam(value = "size") Integer size);

    @Operation(summary = "캘린더 조회")
    ApiResponse<CalendarResponse> getCalendar(
            @LoginMember Long memberId, @RequestParam("yearMonth") YearMonth yearMonth);
