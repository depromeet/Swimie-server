package com.depromeet.pool.port.in.usecase;

import com.depromeet.member.domain.Member;

public interface SearchLogUseCase {
    String createSearchLog(Member member, Long poolId);
}
