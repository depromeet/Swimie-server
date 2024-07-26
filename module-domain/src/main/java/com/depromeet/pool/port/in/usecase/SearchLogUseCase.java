package com.depromeet.pool.port.in.usecase;

import com.depromeet.member.Member;

public interface SearchLogUseCase {
    String createSearchLog(Member member, Long poolId);
}
