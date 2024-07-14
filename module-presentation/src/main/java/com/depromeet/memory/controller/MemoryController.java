package com.depromeet.memory.controller;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.type.memory.MemorySuccessType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/memory")
@RequiredArgsConstructor
public class MemoryController {
    private final MemoryService memoryService;

    @PostMapping
    @Operation(summary = "수영 기록 저장")
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        memoryService.save(memoryCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS));
    }
}
