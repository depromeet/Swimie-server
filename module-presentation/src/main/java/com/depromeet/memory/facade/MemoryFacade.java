package com.depromeet.memory.facade;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.image.service.ImageUploadService;
import com.depromeet.member.Member;
import com.depromeet.member.service.MemberService;
import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequestDto;
import com.depromeet.memory.dto.response.MemoryResponse;
import com.depromeet.memory.service.MemoryService;
import com.depromeet.memory.service.StrokeService;
import com.depromeet.memory.service.TimelineService;
import com.depromeet.pool.service.PoolService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryFacade {
    private final MemberService memberService;
    private final MemoryService memoryService;
    private final StrokeService strokeService;
    private final ImageUploadService imageUploadService;
    private final TimelineService timelineService;
    private final PoolService poolService;

    @Transactional
    public void create(Long memberId, MemoryCreateRequest request) {
        Member writer = memberService.findById(memberId);
        Memory newMemory = memoryService.save(writer, request);
        List<Stroke> strokes = strokeService.saveAll(newMemory, request.getStrokes());
        imageUploadService.addMemoryIdToImages(newMemory, request.getImageIdList());
        poolService.createSearchLog(writer, request.getPoolId());
    }

    @Transactional
    public MemoryResponse update(Long memoryId, MemoryUpdateRequest request) {
        Memory memory = memoryService.findById(memoryId);
        List<Stroke> stokes = strokeService.updateAll(memory, request.getStrokes());
        return MemoryResponse.of(memoryService.update(memoryId, request, stokes));
    }

    public MemoryResponse findById(Long memoryId) {
        return MemoryResponse.of(memoryService.findById(memoryId));
    }

    public CustomSliceResponse<?> getTimelineByMemberIdAndCursorAndDate(
            Long memberId, TimelineRequestDto timelineRequestDto) {
        return timelineService.getTimelineByMemberIdAndCursorAndDate(memberId, timelineRequestDto);
    }
}
