package com.depromeet.memory.mapper;

import com.depromeet.dto.response.CustomSliceResponse;
import com.depromeet.memory.domain.vo.Timeline;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.dto.request.StrokeUpdateRequest;
import com.depromeet.memory.dto.request.TimelineRequest;
import com.depromeet.memory.dto.response.TimelineResponse;
import com.depromeet.memory.port.in.command.CreateMemoryCommand;
import com.depromeet.memory.port.in.command.CreateStrokeCommand;
import com.depromeet.memory.port.in.command.UpdateMemoryCommand;
import com.depromeet.memory.port.in.command.UpdateStrokeCommand;
import com.depromeet.memory.port.in.query.TimelineQuery;
import java.util.List;

public class MemoryMapper {
    public static TimelineQuery toQuery(TimelineRequest request) {
        return new TimelineQuery(
                request.getCursorRecordAt(), request.getDate(), request.isShowNewer());
    }

    public static CreateStrokeCommand toCommand(StrokeCreateRequest request) {
        return new CreateStrokeCommand(request.name(), request.laps(), request.meter());
    }

    public static UpdateStrokeCommand toCommand(StrokeUpdateRequest request) {
        return new UpdateStrokeCommand(
                request.id(), request.name(), request.laps(), request.meter());
    }

    public static CreateMemoryCommand toCommand(MemoryCreateRequest request) {
        return CreateMemoryCommand.builder()
                .poolId(request.getPoolId())
                .item(request.getItem())
                .heartRate(request.getHeartRate())
                .pace(request.getPace())
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
        return UpdateMemoryCommand.builder()
                .poolId(request.getPoolId())
                .item(request.getItem())
                .heartRate(request.getHeartRate())
                .pace(request.getPace())
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

    public static CustomSliceResponse<?> toSliceResponse(Timeline timeline) {
        List<TimelineResponse> result =
                timeline.getTimelineContents().stream()
                        .map(TimelineResponse::mapToTimelineResponseDto)
                        .toList();

        return CustomSliceResponse.builder()
                .content(result)
                .pageSize(timeline.getPageSize())
                .cursorRecordAt(getCursorRecordAtResponse(timeline))
                .hasNext(timeline.isHasNext())
                .build();
    }

    private static String getCursorRecordAtResponse(Timeline timeline) {
        return timeline.getCursorRecordAt() != null
                ? timeline.getCursorRecordAt().toString()
                : null;
    }
}
