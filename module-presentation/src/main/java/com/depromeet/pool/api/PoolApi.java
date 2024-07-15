package com.depromeet.pool.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.pool.dto.response.PoolResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "수영장(Pool)")
public interface PoolApi {
    @Operation(summary = "수영장 검색")
    public ApiResponse<PoolResponseDto> searchPoolsByName(
            @RequestParam(required = false) String query);
}
