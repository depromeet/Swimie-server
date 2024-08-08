package com.depromeet.member.dto.response;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberGender;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberGenderResponse(
        Long id, Integer goal, String name, String email, MemberGender gender) {
    public static MemberGenderResponse of(Member member) {
        return new MemberGenderResponse(
                member.getId(),
                member.getGoal(),
                member.getName(),
                member.getEmail(),
                member.getGender());
    }
}
