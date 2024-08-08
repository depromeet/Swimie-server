package com.depromeet.memory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public record TimelineSliceResponse(
        @Schema(description = "타임라인 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<TimelineResponse> content,
        @Schema(
                        description = "사용자 목표",
                        example = "1000",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Integer goal,
        @Schema(description = "페이지 크기", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
                int pageSize,
        @Schema(
                        description = "커서 기준이 되는 recordAt",
                        example = "2024-07-31",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String cursorRecordAt,
        @Schema(
                        description = "다음 페이지가 존재하는지 확인",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean hasNext) {
    @Builder
    public TimelineSliceResponse {}
}
