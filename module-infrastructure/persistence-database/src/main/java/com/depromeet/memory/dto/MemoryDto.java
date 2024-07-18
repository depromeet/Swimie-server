package com.depromeet.memory.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoryDto {
    private Long id;
    private Long memberId;
    private Long poolId;
    private String poolName;
    private String poolAddress;
    private Long memoryDetailId;
    private String item;
    private Short heartRate;
    private LocalTime pace;
    private Integer kcal;
    private List<StrokeDto> strokes;
    private List<ImageDto> images;
    private LocalDate recordAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private Short lane;
    private String diary;
}
