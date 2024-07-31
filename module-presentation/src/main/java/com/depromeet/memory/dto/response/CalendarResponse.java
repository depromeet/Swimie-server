package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.Stroke;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarResponse {
    private List<CalendarDetailResponse> memories;

    public static CalendarResponse of(List<Memory> memoryDomains) {
        List<CalendarDetailResponse> memories = new ArrayList<>();
        for (Memory memoryDomain : memoryDomains) {
            String type = classifyType(memoryDomain.getStrokes());
            Integer totalDistance =
                    getTotalDistance(memoryDomain.getLane(), memoryDomain.getStrokes());
            List<StrokeResponse> strokes = getStrokeResponses(memoryDomain);
            boolean isAchieved = isAchieved(memoryDomain, totalDistance);

            memories.add(
                    CalendarDetailResponse.of(
                            memoryDomain, type, totalDistance, strokes, isAchieved));
        }
        return new CalendarResponse(memories);
    }

    public static List<StrokeResponse> getStrokeResponses(Memory memoryDomain) {
        return memoryDomain.getStrokes().stream()
                .map(
                        it ->
                                StrokeResponse.builder()
                                        .name(it.getName())
                                        .meter(
                                                it.getMeter() == null
                                                        ? (int) (it.getLaps() * 2)
                                                                * memoryDomain.getPool().getLane()
                                                        : it.getMeter())
                                        .build())
                .toList();
    }

    private static String classifyType(List<Stroke> strokes) {
        if (strokes == null || strokes.isEmpty()) {
            return "NORMAL";
        } else if (strokes.size() == 1) {
            return "SINGLE";
        } else {
            return "MULTI";
        }
    }

    private static Integer getTotalDistance(Short lane, List<Stroke> strokes) {
        int result = 0;
        if (strokes == null || strokes.isEmpty()) {
            return null;
        }
        for (Stroke stroke : strokes) {
            if (stroke.getMeter() != null) {
                result += stroke.getMeter();
            } else {
                result += stroke.getLaps() * lane;
            }
        }
        return result;
    }

    private static boolean isAchieved(Memory memory, Integer totalDistance) {
        if (totalDistance == null) return false;
        return totalDistance >= memory.getMember().getGoal();
    }
}
