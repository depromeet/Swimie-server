package com.depromeet.memory.dto.response;

import com.depromeet.memory.domain.MemoryDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalTime;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemoryDetailResponse(
        Long memoryDetailId, String item, Short heartRate, LocalTime pace, Integer kcal) {
    @Builder
    public MemoryDetailResponse {}

    public static MemoryDetailResponse of(MemoryDetail memoryDetail) {
        return MemoryDetailResponse.builder()
                .memoryDetailId(memoryDetail.getId())
                .item(memoryDetail.getItem())
                .heartRate(memoryDetail.getHeartRate())
                .pace(memoryDetail.getPace())
                .kcal(memoryDetail.getKcal())
                .build();
    }
}
