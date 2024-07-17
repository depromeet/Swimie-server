package com.depromeet.memory.controller;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "수영 기록(Memory)")
public interface MemoryApi {
    @Operation(summary = "수영 기록 저장")
    ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest);
}
