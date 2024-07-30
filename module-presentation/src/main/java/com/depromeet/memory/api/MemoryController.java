package com.depromeet.memory.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequest;
import com.depromeet.memory.dto.response.CalendarResponse;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.facade.MemoryFacade;
import com.depromeet.type.memory.MemorySuccessType;
import jakarta.validation.Valid;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memory")
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
    public ApiResponse<MemoryResponse> read(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId) {
        MemoryResponse response = memoryFacade.findById(memberId, memoryId);
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, response);
    }

    @PatchMapping("/{memoryId}")
    public ApiResponse<MemoryResponse> update(
            @LoginMember Long memberId,
            @PathVariable("memoryId") Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest memoryUpdateRequest) {
        MemoryResponse response = memoryFacade.update(memberId, memoryId, memoryUpdateRequest);
        return ApiResponse.success(MemorySuccessType.PATCH_RESULT_SUCCESS, response);
    }

    @GetMapping("/timeline")
    public ApiResponse<?> timeline(@LoginMember Long memberId, TimelineRequest timelineRequest) {
        CustomSliceResponse<?> result =
                memoryFacade.getTimelineByMemberIdAndCursorAndDate(memberId, timelineRequest);
        return ApiResponse.success(MemorySuccessType.GET_TIMELINE_SUCCESS, result);
    }

    @GetMapping("/calendar")
    public ApiResponse<CalendarResponse> getCalendar(
            @LoginMember Long memberId, @RequestParam("yearMonth") YearMonth yearMonth) {
        CalendarResponse response = memoryFacade.getCalendar(memberId, yearMonth);
        return ApiResponse.success(MemorySuccessType.GET_CALENDAR_SUCCESS, response);
    }
}
