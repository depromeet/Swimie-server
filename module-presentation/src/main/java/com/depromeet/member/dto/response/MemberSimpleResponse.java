package com.depromeet.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberSimpleResponse(Integer goal, String name) {
    public static MemberSimpleResponse from(Integer goal) {
        return new MemberSimpleResponse(goal, null);
    }

    public static MemberSimpleResponse of(Integer goal, String name) {
        return new MemberSimpleResponse(goal, name);
    }
}
