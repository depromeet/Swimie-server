package com.depromeet.type.pool;

import com.depromeet.type.SuccessType;

public enum PoolSuccessType implements SuccessType {
    SEARCH_SUCCESS("POOL_1", "수영장 검색을 성공하였습니다"),
    INITIAL_GET_SUCCESS("POOL_2", "즐겨찾기 및 최근 검색 수영장 조회를 성공하였습니다"),
    FAVORITE_POOL_CREATE_SUCCESS("POOL_3", "수영장 즐겨찾기 등록에 성공하였습니다"),
    FAVORITE_POOL_DELETE_SUCCESS("POOL_4", "수영장 즐겨찾기 삭제에 성공하였습니다");

    private final String code;
    private final String message;

    PoolSuccessType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
