package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.Memory;
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
            String type = memoryDomain.classifyType();
            Integer totalDistance = memoryDomain.calculateTotalDistance();
            List<StrokeResponse> strokes = getStrokeResponses(memoryDomain);
            boolean isAchieved = memoryDomain.isAchieved(totalDistance);

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
}
