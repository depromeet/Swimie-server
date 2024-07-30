package com.depromeet.member.port.in.usecase;

import com.depromeet.member.domain.Member;

public interface GoalUpdateUseCase {
    Member updateGoal(Long memberId, Integer goal);
}
