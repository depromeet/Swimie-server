package com.depromeet.pool.api;

import com.depromeet.config.log.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.facade.PoolFacade;
import com.depromeet.type.pool.PoolSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pool")
public class PoolController implements PoolApi {
    private final PoolFacade poolFacade;

    @GetMapping("/search")
    @Logging(item = "Pool", action = "GET")
    public ApiResponse<PoolSearchResponse> searchPoolsByNameQuery(
            @LoginMember Long memberId,
            @RequestParam(value = "nameQuery") String nameQuery,
            @RequestParam(value = "cursorId", required = false) Long cursorId) {
        return ApiResponse.success(
                PoolSuccessType.SEARCH_SUCCESS,
                poolFacade.findPoolsByName(memberId, nameQuery, cursorId));
    }

    @GetMapping("/search/initial")
    @Logging(item = "Pool", action = "GET")
    public ApiResponse<PoolInitialResponse> getFavoriteAndSearchedPools(
            @LoginMember Long memberId) {
        return ApiResponse.success(
                PoolSuccessType.INITIAL_GET_SUCCESS,
                poolFacade.getFavoriteAndSearchedPools(memberId));
    }

    @PutMapping("/favorite")
    @Logging(item = "Pool", action = "PUT")
    public ApiResponse<?> createFavoritePool(
            @LoginMember Long memberId, @Valid @RequestBody FavoritePoolCreateRequest request) {
        String uri = poolFacade.putFavoritePool(memberId, request);

        if (uri == null) {
            return ApiResponse.success(PoolSuccessType.FAVORITE_POOL_DELETE_SUCCESS);
        }
        return ApiResponse.success(PoolSuccessType.FAVORITE_POOL_CREATE_SUCCESS);
    }
}
