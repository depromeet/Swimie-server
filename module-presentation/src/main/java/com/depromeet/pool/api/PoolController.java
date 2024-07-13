package com.depromeet.pool.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.pool.dto.response.PoolResponseDto;
import com.depromeet.pool.service.PoolService;
import com.depromeet.type.pool.PoolSuccessType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pool")
public class PoolController {
    private final PoolService poolService;

    @GetMapping("/search")
    public ApiResponse<PoolResponseDto> searchPoolsByName(
            @RequestParam(required = false) String query) {
        return ApiResponse.success(
                PoolSuccessType.SEARCH_SUCCESS, poolService.findPoolsByName(query));
    }
}
