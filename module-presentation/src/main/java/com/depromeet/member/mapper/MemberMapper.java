package com.depromeet.member.mapper;

import com.depromeet.auth.dto.response.AccountProfileResponse;
import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.dto.request.MemberCreateRequest;
import com.depromeet.member.port.in.command.SocialMemberCommand;

public class MemberMapper {
    public static Member from(MemberCreateRequest dto) {
        return Member.builder().name(dto.name()).email(dto.email()).role(MemberRole.USER).build();
    }

    public static Member from(String nickname, String email) {
        return Member.builder().name(nickname).email(email).role(MemberRole.USER).build();
    }

    public static SocialMemberCommand toCommand(AccountProfileResponse response) {
        return new SocialMemberCommand(response.id(), response.name(), response.email());
    }
}
