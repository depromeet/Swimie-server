package com.depromeet.reaction.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.reaction.dto.request.ReactionCreateRequest;
import com.depromeet.reaction.dto.response.MemoryReactionResponse;
import com.depromeet.reaction.dto.response.PagingReactionResponse;
import com.depromeet.reaction.dto.response.ValidateReactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "응원(Reaction)")
public interface ReactionApi {
    @Operation(summary = "응원하기")
    ApiResponse<?> create(
            @LoginMember Long memberId, @Valid @RequestBody ReactionCreateRequest request);

    @Operation(summary = "응원 가능 여부 확인")
    ApiResponse<ValidateReactionResponse> validate(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId);

    @Operation(summary = "수영 상세 기록 내 가로 스크롤 응원 조회")
    ApiResponse<MemoryReactionResponse> read(@PathVariable("memoryId") Long memoryId);

    @Operation(summary = "응원 전체 상세 조회")
    ApiResponse<PagingReactionResponse> read(
            @LoginMember Long memberId,
            @PathVariable("memoryId") Long memoryId,
            @Parameter(description = "다음 조회를 위한 커서 ID", example = "21")
                    @RequestParam(value = "cursorId", required = false)
                    Long cursorId);

    @Operation(summary = "응원 삭제")
    ApiResponse<?> delete(@LoginMember Long memberId, @PathVariable("reactionId") Long reactionId);
}
