package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.security.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<?> create(
            @LoginMember Long memberId,
            @Valid @RequestBody MemoryCreateRequest memoryCreateRequest);

    @Operation(summary = "수영 기록 단일 조회")
    ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId);

    @Operation(summary = "수영 기록 수정")
    ApiResponse<MemoryResponse> update(
            @PathVariable("memoryId") Long memoryId,
            @RequestBody MemoryUpdateRequest memoryUpdateRequest);

    @Operation(summary = "타임라인 최신순 조회 및 달력 조회 후, 위/아래 무한 스크롤 구현")
    ApiResponse<?> timelineCalendar(
            @LoginMember Long memberId,
            @Parameter(description = "최초 조회 이후 나온 timeline 리스트 중 가장 마지막 요소의 memory PK")
                    @RequestParam(value = "cursorId", required = false)
                    Long cursorId,
            @Parameter(
                            description =
                                    "최초 조회 이후 나온 timeline 리스트 중 가장 마지막 요소의 memory recordAt, yyyy-MM",
                            example = "2024-07")
                    @RequestParam(value = "cursorRecordAt", required = false)
                    String cursorRecordAt,
            @Parameter(description = "조회하고 싶은 날짜, yyyy-MM", example = "2024-07")
                    @RequestParam(value = "date", required = false)
                    String date,
            @Parameter(
                            description =
                                    "조회하고 싶은 날짜 조회 이후, 날짜 기준 이전 정보를 보고 싶다면 false, 이후 정보를 보고 싶다면 true")
                    @RequestParam(value = "showNewer", required = false)
                    boolean showNewer,
            @Parameter(description = "페이지 크기") @RequestParam(value = "size") Integer size);
}
