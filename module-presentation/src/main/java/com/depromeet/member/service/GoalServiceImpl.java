package com.depromeet.member.service;

import com.depromeet.exception.InternalServerException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.member.repository.GoalRepository;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.type.member.MemberErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;

    @Override
    @Transactional
    public Member update(Long memberId, Integer goal) {
        return goalRepository.update(memberId, goal)
                .orElseThrow(() -> new InternalServerException(MemberErrorType.UPDATE_GOAL_FAILED));
    }
}
