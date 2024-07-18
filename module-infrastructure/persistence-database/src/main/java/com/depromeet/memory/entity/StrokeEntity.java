package com.depromeet.memory.entity;

import com.depromeet.memory.Stroke;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StrokeEntity {
    @Id
    @Column(name = "stroke_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "memory_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemoryEntity memory;

    private String name;

    private Short laps;

    private Integer meter;

    @Builder
    public StrokeEntity(Long id, MemoryEntity memory, String name, Short laps, Integer meter) {
        this.id = id;
        this.memory = memory;
        this.name = name;
        this.laps = laps;
        this.meter = meter;
    }

    public static StrokeEntity from(Stroke stroke) {
        return StrokeEntity.builder()
                .id(stroke.getId())
                .memory(MemoryEntity.from(stroke.getMemory()))
                .name(stroke.getName())
                .laps(stroke.getLaps())
                .meter(stroke.getMeter())
                .build();
    }

    public Stroke toModel() {
        return Stroke.builder()
                .id(this.id)
                .memory(this.memory.toModel())
                .name(this.name)
                .laps(this.laps)
                .meter(this.meter)
                .build();
    }
}
