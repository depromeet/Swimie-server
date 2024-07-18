package com.depromeet.memory.controller;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.exception.InternalServerException;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.TimelineResponseDto;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.memory.service.TimelineService;
import com.depromeet.security.LoginMember;
import com.depromeet.type.memory.MemoryErrorType;
import com.depromeet.type.memory.MemorySuccessType;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memory")
public class MemoryController implements MemoryApi {
    private final MemoryService memoryService;
    private final StrokeService strokeService;
    private final ImageUploadService imageUploadService;
    private final TimelineService timelineService;

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

    @GetMapping("/timeline")
    public ApiResponse<?> getTimeline(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size") int size,
            @LoginMember Long memberId) {
        Slice<TimelineResponseDto> responseDtoSlice =
                timelineService.findTimelineByMemberIdAndCursor(memberId, cursorId, size);

        return ApiResponse.success(MemorySuccessType.GET_TIMELINE_SUCCESS, responseDtoSlice);
    }
}
