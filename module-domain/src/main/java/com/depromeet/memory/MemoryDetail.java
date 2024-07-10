package com.depromeet.memory;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoryDetail {
    private Long id;
    private Memory memory;
    private String item;
    private Short heartRate;
    private LocalTime pace;
    private Integer kcal;

    @Builder
    public MemoryDetail(
            Long id, Memory memory, String item, Short heartRate, LocalTime pace, Integer kcal) {
        this.id = id;
        this.memory = memory;
        this.item = item;
        this.heartRate = heartRate;
        this.pace = pace;
        this.kcal = kcal;
    }
}
