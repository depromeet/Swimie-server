package com.depromeet.pool.port.in.usecase;

import com.depromeet.pool.domain.Pool;
import java.util.List;

public interface PoolSearchUseCase {
    List<Pool> getPoolsByName(String nameQuery);
}
