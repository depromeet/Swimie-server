package com.depromeet.member.mapper;

import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.member.port.in.command.SocialMemberCommand;

public class MemberMapper {
    public static SocialMemberCommand toCommand(
            AccountProfileResponse response, String providerId) {
        return new SocialMemberCommand(
                response.id(), response.name(), response.email(), providerId);
    }
}
