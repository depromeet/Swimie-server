package com.depromeet.memory.dto.response;

import com.depromeet.memory.Memory;
import com.depromeet.memory.Stroke;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class CalendarResponse {
    private LocalDate today = LocalDate.now();
    private Map<Integer, DayResponse> memories = new HashMap<>();

    public void addMemory(Memory memory) {
        int key = memory.getRecordAt().getDayOfMonth();
        String type = classifyType(memory.getStrokes());
        Integer totalDistance = getTotalDistance(memory.getLane(), memory.getStrokes());
        List<StrokeResponse> strokes =
                memory.getStrokes().stream()
                        .map(
                                it ->
                                        StrokeResponse.builder()
                                                .name(it.getName())
                                                .meter(
                                                        it.getMeter() == null
                                                                ? it.getLaps()
                                                                        * memory.getPool().getLane()
                                                                : it.getMeter())
                                                .build())
                        .toList();
        boolean isAchieved = totalDistance >= memory.getMember().getGoal();

        memories.put(key, DayResponse.of(memory, type, totalDistance, strokes, isAchieved));
    }

    private String classifyType(List<Stroke> strokes) {
        if (strokes == null || strokes.isEmpty()) {
            return "NORMAL";
        } else if (strokes.size() == 1) {
            return "SINGLE";
        } else {
            return "MULTI";
        }
    }

    private Integer getTotalDistance(Short lane, List<Stroke> strokes) {
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
}
