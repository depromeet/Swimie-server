package com.depromeet.memory.dto.response;

import com.depromeet.image.domain.vo.MemoryImageUrlVo;
import com.depromeet.member.domain.Member;
import com.depromeet.memory.domain.vo.TimelineSlice;
import com.depromeet.reaction.domain.vo.ReactionCount;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Builder;

public record TimelineSliceResponse(
        @Schema(description = "타임라인 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<TimelineResponse> content,
        @Schema(
                        description = "멤버 목표",
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

    public static TimelineSliceResponse of(
            Member member,
            TimelineSlice timelineSlice,
            List<ReactionCount> reactionCounts,
            Map<Long, MemoryImageUrlVo> memoryImageUrls,
            String imageOrigin) {
        List<TimelineResponse> result =
                timelineSlice.getTimelineContents().stream()
                        .map(
                                memory ->
                                        TimelineResponse.of(
                                                memory,
                                                reactionCounts,
                                                memoryImageUrls,
                                                imageOrigin))
                        .toList();
        return TimelineSliceResponse.builder()
                .content(result)
                .goal(member.getGoal())
                .pageSize(timelineSlice.getPageSize())
                .cursorRecordAt(getCursorRecordAtResponse(timelineSlice))
                .hasNext(timelineSlice.isHasNext())
                .build();
    }

    private static String getCursorRecordAtResponse(TimelineSlice timelineSlice) {
        return timelineSlice.getCursorRecordAt() != null
                ? timelineSlice.getCursorRecordAt().toString()
                : null;
    }
}
