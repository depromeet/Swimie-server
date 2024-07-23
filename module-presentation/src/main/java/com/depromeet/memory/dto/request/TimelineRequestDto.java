package com.depromeet.memory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.YearMonth;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class TimelineRequestDto {
    @Schema(description = "최초 조회 이후 나온 timeline 리스트 중 가장 마지막 요소의 memory PK")
    private Long cursorId;

    @DateTimeFormat(pattern = "yyyy-MM")
    @Schema(description = "조회하고 싶은 날짜, yyyy-MM")
    private YearMonth date;

    @Builder.Default
    @Schema(description = "조회하고 싶은 날짜 조회 이후, 날짜 기준 이전 정보를 보고 싶다면 false, 이후 정보를 보고 싶다면 true")
    private boolean showNewer = false;

    @Min(1)
    @NotNull
    @Schema(description = "페이지 크기")
    private Integer size;
}
