package com.depromeet.memory.dto.response;

import com.depromeet.member.dto.response.MemberSimpleResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public record TimelineSliceResponse(
        @Schema(description = "타임라인 리스트") List<TimelineResponse> content,
        @Schema(description = "멤버 목표", example = "1000") MemberSimpleResponse member,
        @Schema(description = "페이지 크기", example = "10") int pageSize,
        @Schema(description = "커서 기준이 되는 recordAt", example = "2024-07-31") String cursorRecordAt,
        @Schema(description = "다음 페이지가 존재하는지 확인", example = "true") boolean hasNext) {
    @Builder
    public TimelineSliceResponse {}
}
