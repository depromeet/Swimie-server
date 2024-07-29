package com.depromeet.memory.dto.response;

import com.depromeet.image.dto.response.ImagesResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TimelineResponse(
        Long memoryId,
        String recordAt,
        String startTime,
        String endTime,
        Short lane,
        String diary,
        Integer totalMeter,
        Long memoryDetailId,
        String item,
        Short heartRate,
        String pace,
        Integer kcal,
        List<StrokeResponse> strokes,
        List<ImagesResponse> images) {
    @Builder
    public TimelineResponse {}
}
