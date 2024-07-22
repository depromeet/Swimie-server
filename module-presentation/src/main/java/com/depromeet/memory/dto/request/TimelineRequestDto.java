package com.depromeet.memory.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.YearMonth;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class TimelineRequestDto {
    @Schema(description = "최초 조회 이후 나온 timeline 리스트 중 가장 마지막 요소의 memory PK")
    private Long cursorId;

    @Schema(description = "최초 조회 이후 나온 timeline 리스트 중 가장 마지막 요소의 memory recordAt, yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth cursorRecordAt;

    @Schema(description = "조회하고 싶은 날짜, yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth date;

    @Schema(description = "조회하고 싶은 날짜 조회 이후, 날짜 기준 이전 정보를 보고 싶다면 false, 이후 정보를 보고 싶다면 true")
    private boolean showNewer;

    @NotNull
    @Schema(description = "페이지 크기")
    private Integer size;

    public TimelineRequestDto(
            Long cursorId,
            YearMonth cursorRecordAt,
            YearMonth date,
            boolean showNewer,
            Integer size) {
        this.cursorId = cursorId;
        this.cursorRecordAt = cursorRecordAt;
        this.date = date;
        this.showNewer = showNewer;
        this.size = size;
    }
}
