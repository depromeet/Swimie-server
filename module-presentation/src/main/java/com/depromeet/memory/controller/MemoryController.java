package com.depromeet.memory.controller;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.exception.InternalServerException;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.type.memory.MemoryErrorType;
import com.depromeet.type.memory.MemorySuccessType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
    private final ImageUploadService imageUploadService;

    @PostMapping
    @Operation(summary = "수영 기록 저장")
    public ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        Memory newMemory = memoryService.save(memoryCreateRequest);
        if (newMemory == null) {
            return ApiResponse.fail(new InternalServerException(MemoryErrorType.CREATE_FAILED));
        }
        imageUploadService.addMemoryIdToImages(newMemory, memoryCreateRequest.getImageIdList());
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS);
    }
}
