package com.depromeet.memory.mapper;

import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.vo.TimelineSlice;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.dto.request.StrokeUpdateRequest;
import com.depromeet.memory.dto.response.TimelineResponse;
import com.depromeet.memory.dto.response.TimelineSliceResponse;
import com.depromeet.memory.port.in.command.CreateMemoryCommand;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.reaction.domain.vo.ReactionCount;
import java.time.LocalTime;
import java.util.List;

public class MemoryMapper {
    public static CreateStrokeCommand toCommand(StrokeCreateRequest request) {
        return new CreateStrokeCommand(request.name(), request.laps(), request.meter());
    }

    public static UpdateStrokeCommand toCommand(StrokeUpdateRequest request) {
        return new UpdateStrokeCommand(
                request.id(), request.name(), request.laps(), request.meter());
    }

    public static CreateMemoryCommand toCommand(MemoryCreateRequest request) {
        LocalTime pace = getPace(request.getPaceMinutes(), request.getPaceSeconds());
        return CreateMemoryCommand.builder()
                .poolId(request.getPoolId())
                .item(request.getItem())
                .heartRate(request.getHeartRate())
                .pace(pace)
                .kcal(request.getKcal())
                .recordAt(request.getRecordAt())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .lane(request.getLane())
                .diary(request.getDiary())
                .strokes(
                        request.getStrokes() != null
                                ? request.getStrokes().stream()
                                        .map(MemoryMapper::toCommand)
                                        .toList()
                                : null)
                .imageIdList(request.getImageIdList())
                .build();
    }

    public static UpdateMemoryCommand toCommand(MemoryUpdateRequest request) {
        LocalTime pace = getPace(request.getPaceMinutes(), request.getPaceSeconds());
        return UpdateMemoryCommand.builder()
                .poolId(request.getPoolId())
                .item(request.getItem())
                .heartRate(request.getHeartRate())
                .pace(pace)
                .kcal(request.getKcal())
                .recordAt(request.getRecordAt())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .lane(request.getLane())
                .diary(request.getDiary())
                .strokes(
                        request.getStrokes() != null
                                ? request.getStrokes().stream()
                                        .map(MemoryMapper::toCommand)
                                        .toList()
                                : null)
                .build();
    }

    private static LocalTime getPace(Integer minute, Integer second) {
        if (minute != null || second != null) {
            int min = minute != null ? minute : 0;
            int sec = second != null ? second : 0;
            return LocalTime.of(0, min, sec);
        }
        return null;
    }

    public static TimelineSliceResponse toSliceResponse(
            Member member,
            TimelineSlice timelineSlice,
            List<ReactionCount> reactionCounts,
            List<MemoryImageUrlVo> memoryImageUrls,
            String imageOrigin) {
        List<TimelineResponse> result =
                timelineSlice.getTimelineContents().stream()
                        .map(
                                memory ->
                                        TimelineResponse.mapToTimelineResponseDto(
                                                memory,
                                                reactionCounts,
                                                memoryImageUrls,
                                                imageOrigin))
                        .toList();
        return TimelineSliceResponse.builder()
                .content(result)
                .goal(member.getGoal())
                .pageSize(timelineSlice.getPageSize())
                .cursorRecordAt(getCursorRecordAtResponse(timelineSlice))
                .hasNext(timelineSlice.isHasNext())
                .build();
    }

    private static String getCursorRecordAtResponse(TimelineSlice timelineSlice) {
        return timelineSlice.getCursorRecordAt() != null
                ? timelineSlice.getCursorRecordAt().toString()
                : null;
    }
}
