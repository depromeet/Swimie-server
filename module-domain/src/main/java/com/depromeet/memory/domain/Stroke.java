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

    public Float getLapsFromStroke(Short lane) {
        if (this.laps == null) {
            return null;
        }
        int meter = this.meter != null ? this.meter : 0;
        if (this.laps == 0 && meter != 0) {
            return meter / (float) (lane * 2);
        }
        return this.laps;
    }

    public Integer getMeterFromStroke(Short lane) {
        if (this.meter == null) {
            return null;
        }
        float laps = this.laps != null ? this.laps : 0F;
        if (this.meter == 0 && laps != 0) {
            return (int) (laps * lane * 2);
        }
        return this.meter;
    }
}
