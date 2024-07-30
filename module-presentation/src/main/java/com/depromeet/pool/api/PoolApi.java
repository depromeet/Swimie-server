package com.depromeet.pool.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영장(Pool)")
public interface PoolApi {
    @Operation(summary = "수영장 검색")
    ApiResponse<PoolSearchResponse> searchPoolsByNameQuery(
            @LoginMember Long memberId,
            @Schema(description = "수영장 검색 입력값", example = "강남") @RequestParam(value = "nameQuery")
                    String nameQuery,
            @Schema(description = "마지막 수영장 아이디", example = "77")
                    @RequestParam(value = "cursorId", required = false)
                    Long cursorId);

    @Operation(summary = "즐겨찾기 및 최근 검색 수영장 조회")
    ApiResponse<PoolInitialResponse> getFavoriteAndSearchedPools(@LoginMember Long memberId);

    @Operation(summary = "수영장 즐겨찾기 등록 및 삭제")
    ResponseEntity<URI> createFavoritePool(
            @LoginMember Long memberId, @Valid @RequestBody FavoritePoolCreateRequest request);
}
