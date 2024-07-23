package com.depromeet.member.repository;

import com.depromeet.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GoalRepositoryImpl implements GoalRepository {
    private final GoalJpaRepository goalJpaRepository;

    @Override
    public Optional<Member> update(Long memberId, Integer goal) {
        return goalJpaRepository.findById(memberId)
                .map(entity -> entity.updateGoal(goal).toModel());
    }
}
