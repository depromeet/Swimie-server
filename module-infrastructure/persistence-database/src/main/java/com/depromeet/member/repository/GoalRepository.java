package com.depromeet.member.repository;

import com.depromeet.member.Member;

import java.util.Optional;

public interface GoalRepository {
    Optional<Member> update(Long memberId, Integer goal);
}
