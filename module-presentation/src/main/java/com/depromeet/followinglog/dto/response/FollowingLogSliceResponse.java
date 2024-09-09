package com.depromeet.followinglog.dto.response;

import com.depromeet.followinglog.domain.vo.FollowingLogSlice;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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

    public static FollowingLogSliceResponse of(
            FollowingLogSlice followingLogSlice,
            LocalDateTime lastViewedFollowingLogAt,
            String profileImageOrigin) {
        List<FollowingLogMemoryResponse> contents =
                followingLogSlice.getContents().stream()
                        .map(
                                followingMemoryLog ->
                                        FollowingLogMemoryResponse.of(
                                                followingMemoryLog,
                                                lastViewedFollowingLogAt,
                                                profileImageOrigin))
                        .toList();
        return FollowingLogSliceResponse.builder()
                .content(contents)
                .pageSize(followingLogSlice.getPageSize())
                .cursorId(followingLogSlice.getCursorId())
                .hasNext(followingLogSlice.isHasNext())
                .build();
    }
}
