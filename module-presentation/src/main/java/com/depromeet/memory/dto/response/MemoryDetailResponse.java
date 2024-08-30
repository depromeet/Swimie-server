package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.MemoryDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemoryDetailResponse(
        String item, Short heartRate, Integer paceMinutes, Integer paceSeconds, Integer kcal) {
    @Builder
    public MemoryDetailResponse {}

    public static MemoryDetailResponse of(MemoryDetail memoryDetail) {
        return MemoryDetailResponse.builder()
                .item(memoryDetail.getItem())
                .heartRate(memoryDetail.getHeartRate())
                .paceMinutes(getMinute(memoryDetail))
                .paceSeconds(getSecond(memoryDetail))
                .kcal(memoryDetail.getKcal())
                .build();
    }

    private static Integer getSecond(MemoryDetail memoryDetail) {
        return memoryDetail.getPace() != null ? memoryDetail.getPace().getSecond() : null;
    }

    private static Integer getMinute(MemoryDetail memoryDetail) {
        return memoryDetail.getPace() != null ? memoryDetail.getPace().getMinute() : null;
    }
}
