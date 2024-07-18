package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest);

    @Operation(summary = "수영 기록 단일 조회")
    ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId);

    @Operation(summary = "수영 기록 수정")
    ApiResponse<MemoryResponse> update(
            @PathVariable("memoryId") Long memoryId,
            @RequestBody MemoryUpdateRequest memoryUpdateRequest);
}
