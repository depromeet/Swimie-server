package com.depromeet.memory.facade;

import static com.depromeet.memory.service.MemoryValidator.validatePermission;

import com.depromeet.exception.BadRequestException;
import com.depromeet.followinglog.port.in.FollowingMemoryLogUseCase;
import com.depromeet.followinglog.port.in.command.CreateFollowingMemoryCommand;
import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import com.depromeet.image.port.in.ImageGetUseCase;
import com.depromeet.image.port.in.ImageUpdateUseCase;
import com.depromeet.image.port.in.ImageUploadUseCase;
import com.depromeet.member.domain.Member;
import com.depromeet.member.port.in.usecase.MemberUseCase;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.memory.domain.vo.MemoryInfo;
import com.depromeet.memory.domain.vo.TimelineSlice;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.*;
import com.depromeet.memory.mapper.MemoryMapper;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.memory.port.in.usecase.*;
import com.depromeet.notification.port.in.usecase.DeleteReactionLogUseCase;
import com.depromeet.pool.port.in.usecase.SearchLogUseCase;
import com.depromeet.reaction.domain.vo.ReactionCount;
import com.depromeet.reaction.port.in.usecase.DeleteReactionUseCase;
import com.depromeet.reaction.port.in.usecase.GetReactionUseCase;
import com.depromeet.type.memory.MemoryErrorType;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryFacade {
    private final MemberUseCase memberUseCase;
    private final StrokeUseCase strokeUseCase;
    private final ImageGetUseCase imageGetUseCase;
    private final CalendarUseCase calendarUseCase;
    private final TimelineUseCase timelineUseCase;
    private final GetMemoryUseCase getMemoryUseCase;
    private final ImageUploadUseCase imageUploadUseCase;
    private final SearchLogUseCase poolSearchLogUseCase;
    private final CreateMemoryUseCase createMemoryUseCase;
    private final UpdateMemoryUseCase updateMemoryUseCase;
    private final DeleteMemoryUseCase deleteMemoryUseCase;
    private final GetReactionUseCase getReactionUseCase;
    private final ImageUpdateUseCase imageUpdateUseCase;
    private final ApplicationEventPublisher eventPublisher;
    private final DeleteReactionUseCase deleteReactionUseCase;
    private final DeleteReactionLogUseCase deleteReactionLogUseCase;
    private final FollowingMemoryLogUseCase followingMemoryLogUseCase;

    @Value("${cloud-front.domain}")
    private String imageOrigin;

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
        // 팔로잉 소식 저장
        eventPublisher.publishEvent(new CreateFollowingMemoryCommand(newMemory));
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

    public MemoryResponse findById(Long requestMemberId, Long memoryId) {
        MemoryInfo memoryInfo = getMemoryUseCase.findByIdWithPrevNext(requestMemberId, memoryId);
        return MemoryResponse.from(memoryInfo);
    }

    public TimelineSliceResponse getTimelineByMemberIdAndCursorAndDate(
            Long memberId, LocalDate cursorRecordAt) {
        Member member = memberUseCase.findById(memberId);
        TimelineSlice timelineSlice =
                timelineUseCase.getTimelineByMemberIdAndCursorAndDate(memberId, cursorRecordAt);
        List<Long> memoryIds =
                timelineSlice.getTimelineContents().stream()
                        .mapToLong(Memory::getId)
                        .boxed()
                        .toList();
        List<MemoryImageUrlVo> memoryImageUrls = imageGetUseCase.findImagesByMemoryIds(memoryIds);
        List<ReactionCount> reactionCounts =
                getReactionUseCase.getDetailReactionsCountByMemoryIds(memoryIds);

        return MemoryMapper.toSliceResponse(
                member, timelineSlice, reactionCounts, memoryImageUrls, imageOrigin);
    }

    public CalendarResponse getCalendar(Long memberId, Long targetId, Integer year, Short month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        Member member = memberUseCase.findById(getTargetMemberId(memberId, targetId));
        List<Memory> calendarMemories =
                calendarUseCase.getCalendarByYearAndMonth(
                        getTargetMemberId(memberId, targetId), yearMonth);
        return CalendarResponse.of(member, calendarMemories);
    }

    private Long getTargetMemberId(Long memberId, Long targetId) {
        return targetId == null ? memberId : targetId;
    }

    @Transactional
    public void deleteById(Long memberId, Long memoryId) {
        Memory memory = getMemoryUseCase.findById(memoryId);
        if (memory.getMember().getId().equals(memberId)) {
            // Following memory log 삭제
            followingMemoryLogUseCase.deleteAllByMemoryId(memoryId);
            // Reaction 조회
            List<Long> reactionIds = getReactionUseCase.findAllIdByMemoryId(memoryId);
            // Reaction log 삭제
            deleteReactionLogUseCase.deleteAllByReactionId(reactionIds);
            // Reaction 삭제
            deleteReactionUseCase.deleteAllById(reactionIds);
            // Stroke 삭제
            strokeUseCase.deleteAllByMemoryId(memoryId);
            // Image FK Null
            imageUpdateUseCase.setNullByMemoryId(memoryId);
            // Memory 삭제
            deleteMemoryUseCase.deleteById(memoryId);
            // MemoryDetail 삭제
            if (memory.getMemoryDetail() != null) {
                Long memoryDetailId = memory.getMemoryDetail().getId();
                deleteMemoryUseCase.deleteMemoryDetailById(memoryDetailId);
            }
        } else {
            throw new BadRequestException(MemoryErrorType.ONLY_OWNER_CAN_DELETE_MEMORY);
        }
    }
}
