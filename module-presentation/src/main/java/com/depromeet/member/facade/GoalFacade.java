package com.depromeet.member.facade;

import com.depromeet.member.Member;
import com.depromeet.member.dto.response.MemberSimpleResponse;
import com.depromeet.member.service.GoalService;
import com.depromeet.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalFacade {
    private final GoalService goalService;
    private final MemberService memberService;

    public MemberSimpleResponse update(Long memberId, Integer goal) {
        Member member = goalService.update(memberId, goal);
        return new MemberSimpleResponse(member.getGoal(), member.getName());
    }

    public MemberSimpleResponse findGoalById(Long memberId) {
        Member member = memberService.findById(memberId);
        return new MemberSimpleResponse(member.getGoal(), member.getName());
    }
}
