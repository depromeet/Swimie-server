package com.depromeet.blacklist.dto.response;

import com.depromeet.blacklist.domain.vo.BlacklistPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlackMemberResponse(
        @Schema(description = "차단한 유저 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
                List<BlackMemberDetailResponse> blackMembers,
        @Schema(
                        description = "다음 페이지가 존재하는지 여부",
                        example = "true",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                boolean hasNext,
        @Schema(
                        description = "다음 페이지 검색 커서 ID(hasNext가 false일 시 null)",
                        example = "11",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                Long cursorId) {
    public static BlackMemberResponse of(BlacklistPage page, String domain) {
        return new BlackMemberResponse(
                BlackMemberDetailResponse.of(page.blackMembers(), domain),
                page.hasNext(),
                page.cursorId());
    }
}
