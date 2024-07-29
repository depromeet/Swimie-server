package com.depromeet.memory.dto.response;

import com.depromeet.memory.Memory;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DayResponse(
        String type,
        Integer totalDistance,
        List<StrokeResponse> strokes,
        String imageUrl,
        boolean isAchieved) {
    public static DayResponse of(
            Memory memory,
            String type,
            Integer totalDistance,
            List<StrokeResponse> strokes,
            boolean isAchieved) {
        String imageUrl = null;
        if (!memory.getImages().isEmpty()) {
            imageUrl = memory.getImages().getFirst().getImageUrl();
        }
        return new DayResponse(type, totalDistance, strokes, imageUrl, isAchieved);
    }
}
