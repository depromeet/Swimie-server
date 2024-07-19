package com.depromeet.pool.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.security.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영장(Pool)")
public interface PoolApi {
    @Operation(summary = "수영장 검색")
    ApiResponse<PoolSearchResponse> searchPoolsByNameQuery(
            @Schema(description = "수영장 검색 입력값", example = "강남") @RequestParam(value = "nameQuery")
                    String nameQuery);

    @Operation(summary = "즐겨찾기 및 최근 검색 수영장 조회")
    ApiResponse<PoolInitialResponse> getFavoriteAndSearchedPools(@LoginMember Long memberId);
}
