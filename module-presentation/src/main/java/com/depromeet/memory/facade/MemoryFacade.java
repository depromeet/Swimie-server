package com.depromeet.memory.facade;

import static com.depromeet.memory.service.MemoryValidator.validatePermission;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.image.port.in.ImageUploadUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.domain.vo.Timeline;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequest;
import com.depromeet.memory.dto.response.CalendarResponse;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.mapper.MemoryMapper;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.memory.port.in.usecase.TimelineUseCase;
import com.depromeet.memory.service.CalendarService;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.pool.port.in.usecase.SearchLogUseCase;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryFacade {
    private final MemberUseCase memberUseCase;
    private final MemoryService memoryService;
    private final StrokeService strokeService;
    private final TimelineUseCase timelineUseCase;
    private final CalendarService calendarService;
    private final ImageUploadUseCase imageUploadUseCase;
    private final SearchLogUseCase poolSearchLogUseCase;

    @Transactional
    public void create(Long memberId, MemoryCreateRequest request) {
        Member writer = memberUseCase.findById(memberId);
        Memory newMemory = memoryService.save(writer, MemoryMapper.toCommand(request));

        List<CreateStrokeCommand> commands =
                request.getStrokes().stream().map(MemoryMapper::toCommand).toList();
        List<Stroke> strokes = strokeService.saveAll(newMemory, commands);

        imageUploadUseCase.changeImageStatusAndAddMemoryIdToImages(
                newMemory, request.getImageIdList());

        poolSearchLogUseCase.createSearchLog(writer, request.getPoolId());
    }

    @Transactional
    public MemoryResponse update(Long memberId, Long memoryId, MemoryUpdateRequest request) {
        Memory memory = memoryService.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);

        List<UpdateStrokeCommand> commands =
                request.getStrokes().stream().map(MemoryMapper::toCommand).toList();
        List<Stroke> strokes = strokeService.updateAll(memory, commands);
        UpdateMemoryCommand command = MemoryMapper.toCommand(request);

        return MemoryResponse.from(memoryService.update(memoryId, command, strokes));
    }

    public MemoryResponse findById(Long memberId, Long memoryId) {
        Memory memory = memoryService.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);
        return MemoryResponse.from(memory);
    }

    public CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId, TimelineRequest request) {
        Timeline timeline =
                timelineUseCase.getTimelineByMemberIdAndCursorAndDate(
                        memberId, MemoryMapper.toQuery(request));

        return MemoryMapper.toSliceResponse(timeline);
    }

    public CalendarResponse getCalendar(Long memberId, YearMonth yearMonth) {
        List<Memory> calendarMemories =
                calendarService.getCalendarByYearAndMonth(memberId, yearMonth);
        return CalendarResponse.of(calendarMemories);
    }
}
