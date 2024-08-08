package com.depromeet.memory.facade;

import static com.depromeet.memory.service.MemoryValidator.validatePermission;

import com.depromeet.image.port.in.ImageUploadUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.domain.vo.Timeline;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.*;
import com.depromeet.memory.mapper.MemoryMapper;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.memory.port.in.usecase.CalendarUseCase;
import com.depromeet.memory.port.in.usecase.CreateMemoryUseCase;
import com.depromeet.memory.port.in.usecase.GetMemoryUseCase;
import com.depromeet.memory.port.in.usecase.StrokeUseCase;
import com.depromeet.memory.port.in.usecase.TimelineUseCase;
import com.depromeet.memory.port.in.usecase.UpdateMemoryUseCase;
import com.depromeet.pool.port.in.usecase.SearchLogUseCase;
import java.time.LocalDate;
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
    private final StrokeUseCase strokeUseCase;
    private final CalendarUseCase calendarUseCase;
    private final TimelineUseCase timelineUseCase;
    private final GetMemoryUseCase getMemoryUseCase;
    private final ImageUploadUseCase imageUploadUseCase;
    private final SearchLogUseCase poolSearchLogUseCase;
    private final CreateMemoryUseCase createMemoryUseCase;
    private final UpdateMemoryUseCase updateMemoryUseCase;

    @Transactional
    public MemoryCreateResponse create(Long memberId, MemoryCreateRequest request) {
        Member writer = memberUseCase.findById(memberId);
        Memory newMemory = createMemoryUseCase.save(writer, MemoryMapper.toCommand(request));
        Long memoryId = newMemory.getId();
        int month = request.getRecordAt().getMonth().getValue();
        int rank = getMemoryUseCase.findOrderInMonth(memberId, memoryId, month);

        List<CreateStrokeCommand> commands =
                request.getStrokes().stream().map(MemoryMapper::toCommand).toList();
        List<Stroke> strokes = strokeUseCase.saveAll(newMemory, commands);

        imageUploadUseCase.changeImageStatusAndAddMemoryIdToImages(
                newMemory, request.getImageIdList());

        if (request.getPoolId() != null) {
            poolSearchLogUseCase.createSearchLog(writer, request.getPoolId());
        }

        return MemoryCreateResponse.of(month, rank, memoryId);
    }

    @Transactional
    public MemoryResponse update(Long memberId, Long memoryId, MemoryUpdateRequest request) {
        Memory memory = getMemoryUseCase.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);

        List<UpdateStrokeCommand> commands =
                request.getStrokes().stream().map(MemoryMapper::toCommand).toList();
        List<Stroke> strokes = strokeUseCase.updateAll(memory, commands);
        UpdateMemoryCommand command = MemoryMapper.toCommand(request);

        return MemoryResponse.from(updateMemoryUseCase.update(memoryId, command, strokes));
    }

    public MemoryReadUpdateResponse getMemoryForUpdate(Long memberId, Long memoryId) {
        Memory memory = getMemoryUseCase.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);
        return MemoryReadUpdateResponse.from(memory);
    }

    public MemoryResponse findById(Long memberId, Long memoryId) {
        Memory memory = getMemoryUseCase.findById(memoryId);
        validatePermission(memory.getMember().getId(), memberId);
        return MemoryResponse.from(memory);
    }

    public TimelineSliceResponse getTimelineByMemberIdAndCursorAndDate(
            Long memberId, LocalDate cursorRecordAt, YearMonth date, boolean showNewer) {
        Member member = memberUseCase.findById(memberId);
        Timeline timeline =
                timelineUseCase.getTimelineByMemberIdAndCursorAndDate(
                        memberId, cursorRecordAt, date, showNewer);

        return MemoryMapper.toSliceResponse(member, timeline);
    }

    public CalendarResponse getCalendar(Long memberId, Integer year, Short month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        Member member = memberUseCase.findById(memberId);
        List<Memory> calendarMemories =
                calendarUseCase.getCalendarByYearAndMonth(memberId, yearMonth);
        return CalendarResponse.of(member, calendarMemories);
    }
}
