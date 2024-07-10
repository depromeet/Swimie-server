package com.depromeet.memory;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Stroke {
    private Long id;
    private Memory memory;
    private String name;
    private Short laps;
    private Integer meter;

    @Builder
    public Stroke(Long id, Memory memory, String name, Short laps, Integer meter) {
        this.id = id;
        this.memory = memory;
        this.name = name;
        this.laps = laps;
        this.meter = meter;
    }
}
