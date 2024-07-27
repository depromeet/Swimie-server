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
    @Schema(description = "수영장 정보 Id", example = "null")
    private Long poolId; // 수영장 정보

    // MemoryDetail
    @Schema(description = "수영 장비", example = "오리발")
    private String item;

    @Schema(description = "심박수", example = "129")
    private Short heartRate;

    @Schema(description = "페이스", example = "05:00:00", maxLength = 8, type = "string")
    private LocalTime pace;

    @Schema(description = "칼로리", example = "300")
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

    @Schema(description = "레인 길이", example = "25")
    private Short lane;

    @Schema(description = "수영 일기", example = "나는 짱이야!! 내가 정말 멋져!!")
    private String diary;

    // Stroke
    @Schema(description = "영법 목록")
    private List<StrokeCreateRequest> strokes;

    // Images
    @Schema(description = "수영 사진")
    private List<Long> imageIdList;

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
            List<StrokeCreateRequest> strokes,
            List<Long> imageIdList) {
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
        this.imageIdList = imageIdList;
    }
}
