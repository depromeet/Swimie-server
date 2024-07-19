package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.memory.service.TimelineService;
import com.depromeet.security.LoginMember;
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
    private final TimelineService timelineService;

    @PostMapping
    public ApiResponse<?> create(@Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        Memory newMemory = memoryService.save(memoryCreateRequest);
        List<Stroke> strokes = strokeService.saveAll(newMemory, memoryCreateRequest.getStrokes());
        imageUploadService.addMemoryIdToImages(newMemory, memoryCreateRequest.getImageIdList());
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS);
    }

    @GetMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId) {
        MemoryResponse memoryResponse = memoryService.findById(memoryId);
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, memoryResponse);
    }

    @GetMapping("/timeline-calendar")
    public ApiResponse<?> timelineCalendar(
            @LoginMember Long memberId,
            @RequestParam(value = "cursorId", required = false) Long cursorId,
            @RequestParam(value = "cursorRecordAt", required = false) String cursorRecordAt,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "showNewer", required = false) boolean showNewer,
            @RequestParam(value = "size") Integer size) {
        CustomSliceResponse<?> result =
                timelineService.getTimelineByMemberIdAndCursorAndDate(
                        memberId, cursorId, cursorRecordAt, date, showNewer, size);
        return ApiResponse.success(MemorySuccessType.GET_TIMELINE_SUCCESS, result);
    }
}
