package com.depromeet.memory.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Stroke {
    private Long id;
    private Memory memory;
    private String name;
    private Float laps;
    private Integer meter;

    @Builder
    public Stroke(Long id, Memory memory, String name, Float laps, Integer meter) {
        this.id = id;
        this.memory = memory;
        this.name = name;
        this.laps = laps;
        this.meter = meter;
    }
}
