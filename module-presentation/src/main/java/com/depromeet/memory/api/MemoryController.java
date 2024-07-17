package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.type.memory.MemoryErrorType;
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

    @PostMapping
    public ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        Memory newMemory = memoryService.save(memoryCreateRequest);
        if (newMemory == null) {
            return ApiResponse.fail(new InternalServerException(MemoryErrorType.CREATE_FAILED));
        }
        List<Stroke> strokes = strokeService.saveAll(newMemory, memoryCreateRequest.getStrokes());
        imageUploadService.addMemoryIdToImages(newMemory, memoryCreateRequest.getImageIdList());
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS);
    }

    @GetMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId) {
        MemoryResponse memoryResponse = memoryService.findById(memoryId);
        if (memoryResponse == null) {
            return ApiResponse.fail(
                    new NotFoundException(MemoryErrorType.NOT_FOUND), memoryResponse);
        }
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, memoryResponse);
    }
}