package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequestDto;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.facade.MemoryFacade;
import com.depromeet.security.LoginMember;
import com.depromeet.type.memory.MemorySuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memory")
public class MemoryController implements MemoryApi {
    private final MemoryFacade memoryFacade;

    @PostMapping
    public ApiResponse<?> create(
            @LoginMember Long memberId,
            @Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        memoryFacade.create(memberId, memoryCreateRequest);
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS);
    }

    @GetMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> read(@PathVariable("memoryId") Long memoryId) {
        MemoryResponse response = memoryFacade.findById(memoryId);
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, response);
    }

    @PatchMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> update(
            @PathVariable("memoryId") Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest memoryUpdateRequest) {
        MemoryResponse response = memoryFacade.update(memoryId, memoryUpdateRequest);
        return ApiResponse.success(MemorySuccessType.PATCH_RESULT_SUCCESS, response);
    }

    @GetMapping("/timeline")
    public ApiResponse<?> timelineCalendar(
            @LoginMember Long memberId, @ModelAttribute TimelineRequestDto timelineRequestDto) {
        CustomSliceResponse<?> result =
                memoryFacade.getTimelineByMemberIdAndCursorAndDate(memberId, timelineRequestDto);
        return ApiResponse.success(MemorySuccessType.GET_TIMELINE_SUCCESS, result);
    }
}
