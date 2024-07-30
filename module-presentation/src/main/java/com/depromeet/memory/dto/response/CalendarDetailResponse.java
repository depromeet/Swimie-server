package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.Memory;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CalendarDetailResponse(
        Long memoryId,
        int memoryDate,
        String type,
        Integer totalDistance,
        String imageUrl,
        boolean isAchieved,
        List<StrokeResponse> strokes) {
    public static CalendarDetailResponse of(
            Memory memory,
            String type,
            Integer totalDistance,
            List<StrokeResponse> strokes,
            boolean isAchieved) {
        return new CalendarDetailResponse(
                memory.getId(),
                memory.getRecordAt().getDayOfMonth(),
                type,
                totalDistance,
                memory.getImages().isEmpty() ? null : memory.getImages().getFirst().getImageUrl(),
                isAchieved,
                strokes);
    }
}
