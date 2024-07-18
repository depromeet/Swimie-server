package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.image.service.ImageUpdateService;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.type.memory.MemorySuccessType;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memory")
public class MemoryController implements MemoryApi {
    private final MemoryService memoryService;
    private final StrokeService strokeService;
    private final ImageUploadService imageUploadService;
    private final ImageUpdateService imageUpdateService;

    @PostMapping
    public ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        Memory newMemory = memoryService.save(memoryCreateRequest);
        List<Stroke> strokes = strokeService.saveAll(newMemory, memoryCreateRequest.getStrokes());
        imageUploadService.addMemoryIdToImages(newMemory, memoryCreateRequest.getImageIdList());
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS);
    }

    @GetMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId) {
        MemoryResponse memoryResponse = MemoryResponse.from(memoryService.findById(memoryId));
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, memoryResponse);
    }

    @PatchMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> update(
            @PathVariable("memoryId") Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest memoryUpdateRequest) {
        Memory memory = memoryService.findById(memoryId);
        List<Stroke> stokes = strokeService.updateAll(memory, memoryUpdateRequest.getStrokes());
        MemoryResponse memoryResponse =
                MemoryResponse.from(memoryService.update(memoryId, memoryUpdateRequest, stokes));
        return ApiResponse.success(MemorySuccessType.PATCH_RESULT_SUCCESS, memoryResponse);
    }
}
