package com.depromeet.memory.dto.request;

import com.depromeet.exception.BadRequestException;
import com.depromeet.type.memory.MemoryErrorType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class MemoryUpdateRequest {
    @Schema(description = "수영장 정보 Id", example = "null")
    private Long poolId; // 수영장 정보

    // MemoryDetail
    @Schema(description = "수영 장비", example = "오리발")
    private String item;

    @Min(0)
    @Schema(description = "심박수", example = "129")
    private Short heartRate;

    @Min(0)
    @Schema(description = "페이스 분", example = "5", maxLength = 2, type = "int")
    private int paceMinutes;

    @Min(0)
    @Schema(description = "페이스 초", example = "30", maxLength = 2, type = "int")
    private int paceSeconds;

    @Min(0)
    @Schema(description = "칼로리", example = "300")
    private Integer kcal;

    // Memory
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "수영을 한 날짜를 입력하세요")
    private LocalDate recordAt;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "수영을 시작한 시간을 입력하세요")
    @Schema(description = "수영 시작 시간", example = "11:00", maxLength = 5, type = "string")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "수영을 종료한 시간을 입력하세요")
    @Schema(description = "수영 종료 시간", example = "11:50", maxLength = 5, type = "string")
    private LocalTime endTime;

    @Min(0)
    @Schema(description = "레인 길이", example = "25")
    private Short lane;

    @Schema(description = "수영 일기", example = "나는 짱이야!! 내가 정말 멋져!!")
    private String diary;

    // Stroke
    @Valid
    @Schema(description = "영법 목록")
    private List<StrokeUpdateRequest> strokes;

    @Builder
    public MemoryUpdateRequest(
            Long poolId,
            String item,
            Short heartRate,
            int paceMinutes,
            int paceSeconds,
            Integer kcal,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary,
            List<StrokeUpdateRequest> strokes) {
        validateTime();
        this.poolId = poolId;
        this.item = item;
        this.heartRate = heartRate;
        this.paceMinutes = paceMinutes;
        this.paceSeconds = paceSeconds;
        this.kcal = kcal;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
        this.strokes = strokes;
    }

    private void validateTime() {
        if (startTime.isAfter(endTime)) {
            throw new BadRequestException(MemoryErrorType.TIME_NOT_VALID);
        }
    }
}
