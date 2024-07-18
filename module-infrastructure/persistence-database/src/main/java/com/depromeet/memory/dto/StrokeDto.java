package com.depromeet.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StrokeDto {
    private Long id;
    private String name;
    private Short laps;
    private Integer meter;
}
