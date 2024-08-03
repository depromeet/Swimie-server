package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.MemoryDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemoryDetailResponse(
        String item, Short heartRate, int paceMinutes, int paceSeconds, Integer kcal) {
    @Builder
    public MemoryDetailResponse {}

    public static MemoryDetailResponse of(MemoryDetail memoryDetail) {
        return MemoryDetailResponse.builder()
                .item(memoryDetail.getItem())
                .heartRate(memoryDetail.getHeartRate())
                .paceMinutes(memoryDetail.getPace().getMinute())
                .paceSeconds(memoryDetail.getPace().getSecond())
                .kcal(memoryDetail.getKcal())
                .build();
    }
}
