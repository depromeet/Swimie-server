package com.depromeet.pool.service;

import com.depromeet.pool.dto.response.PoolResponseDto;

public interface PoolService {
    PoolResponseDto findPoolsByName(String requestPoolName);
}
