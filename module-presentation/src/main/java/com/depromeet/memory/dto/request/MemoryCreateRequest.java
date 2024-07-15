package com.depromeet.memory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class MemoryCreateRequest {
    private Long poolId; // 수영장 정보
    // MemoryDetail
    private String item;
    private Short heartRate;

    @Schema(description = "페이스", example = "05:00:00", maxLength = 8, type = "string")
    private LocalTime pace;

    private Integer kcal;

    // Memory
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "수영을 한 날짜를 입력하세요")
    private LocalDate recordAt;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @NotNull(message = "수영을 시작한 시간을 입력하세요")
    @Schema(description = "수영 시작 시간", example = "11:00:00", maxLength = 8, type = "string")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @NotNull(message = "수영을 종료한 시간을 입력하세요")
    @Schema(description = "수영 종료 시간", example = "11:50:00", maxLength = 8, type = "string")
    private LocalTime endTime;

    private Short lane;
    private String diary;
    // Stroke
    private List<StrokeCreateRequest> strokes;

    @Builder
    public MemoryCreateRequest(
            Long poolId,
            String item,
            Short heartRate,
            LocalTime pace,
            Integer kcal,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary,
            List<StrokeCreateRequest> strokes) {
        this.poolId = poolId;
        this.item = item;
        this.heartRate = heartRate;
        this.pace = pace;
        this.kcal = kcal;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
        this.strokes = strokes;
    }
}
