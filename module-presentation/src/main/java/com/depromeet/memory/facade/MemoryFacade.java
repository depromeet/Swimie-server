package com.depromeet.memory.facade;

import static com.depromeet.memory.service.MemoryValidator.*;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.member.Member;
import com.depromeet.member.service.MemberService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequestDto;
import com.depromeet.memory.dto.response.CalendarResponse;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.service.CalendarService;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.memory.service.TimelineService;
import com.depromeet.pool.service.PoolService;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryFacade {
    private final PoolService poolService;
    private final MemberService memberService;
    private final MemoryService memoryService;
    private final StrokeService strokeService;
    private final TimelineService timelineService;
    private final CalendarService calendarService;
    private final ImageUploadService imageUploadService;

    @Transactional
    public void create(Long memberId, MemoryCreateRequest request) {
        Member writer = memberService.findById(memberId);
        Memory newMemory = memoryService.save(writer, request);
        List<Stroke> strokes = strokeService.saveAll(newMemory, request.getStrokes());
        imageUploadService.addMemoryIdToImages(newMemory, request.getImageIdList());
        poolService.createSearchLog(writer, request.getPoolId());
    }

    @Transactional
    public MemoryResponse update(Long memberId, Long memoryId, MemoryUpdateRequest request) {
        Memory memory = memoryService.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);
        List<Stroke> strokes = strokeService.updateAll(memory, request.getStrokes());
        return MemoryResponse.from(memoryService.update(memoryId, request, strokes));
    }

    public MemoryResponse findById(Long memberId, Long memoryId) {
        Memory memory = memoryService.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);
        return MemoryResponse.from(memory);
    }

    public CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId, TimelineRequestDto timelineRequestDto) {
        return timelineService.getTimelineByMemberIdAndCursorAndDate(memberId, timelineRequestDto);
    }

    public CalendarResponse getCalendar(Long memberId, YearMonth yearMonth) {
        List<Memory> calendarMemories =
                calendarService.getCalendarByYearAndMonth(memberId, yearMonth);

        CalendarResponse response = new CalendarResponse();
        for (Memory calendarMemory : calendarMemories) {
            response.addMemory(calendarMemory);
        }
        return response;
    }
}
