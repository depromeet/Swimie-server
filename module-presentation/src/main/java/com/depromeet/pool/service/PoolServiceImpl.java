package com.depromeet.pool.service;

import com.depromeet.pool.Pool;
import com.depromeet.pool.dto.response.PoolResponseDto;
import com.depromeet.pool.repository.PoolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PoolServiceImpl implements PoolService {
    private final PoolRepository poolRepository;

    @Override
    public PoolResponseDto findPoolsByName(String query) {
        List<Pool> findPools = poolRepository.findPoolsByName(query);
        return PoolResponseDto.of(findPools);
    }
}
