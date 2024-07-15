package com.depromeet.memory.controller;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.type.memory.MemorySuccessType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memory")
public class MemoryController {
    private final MemoryService memoryService;
    private final StrokeService strokeService;

    @PostMapping
    @Operation(summary = "수영 기록 저장")
    public ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        Memory newMemory = memoryService.save(memoryCreateRequest);
        List<Stroke> strokes = strokeService.saveAll(newMemory, memoryCreateRequest.getStrokes());
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS);
    }
}
