package com.depromeet.followingLog.dto.response;

import com.depromeet.followingLog.domain.FollowingLogSlice;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowingLogSliceResponse {
    @Schema(description = "팔로잉 소식 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<FollowingLogMemoryResponse> content;

    @Schema(description = "페이지 크기", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize;

    @Schema(
            description = "cursorId",
            example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long cursorId;

    @Schema(
            description = "다음 페이지가 존재하지 유무",
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean hasNext;

    @Builder
    public FollowingLogSliceResponse(
            List<FollowingLogMemoryResponse> content,
            int pageSize,
            Long cursorId,
            boolean hasNext) {
        this.content = content;
        this.pageSize = pageSize;
        this.cursorId = cursorId;
        this.hasNext = hasNext;
    }

    public static FollowingLogSliceResponse toFollowingLogSliceResponse(
            FollowingLogSlice followingLogSlice) {
        List<FollowingLogMemoryResponse> contents =
                followingLogSlice.getContents().stream()
                        .map(FollowingLogMemoryResponse::toFollowingLogMemoryResponse)
                        .toList();
        return FollowingLogSliceResponse.builder()
                .content(contents)
                .pageSize(followingLogSlice.getPageSize())
                .cursorId(followingLogSlice.getCursorId())
                .hasNext(followingLogSlice.isHasNext())
                .build();
    }
}
