package com.depromeet.member.mapper;

import com.depromeet.auth.domain.AccountType;
import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.member.port.in.command.SocialMemberCommand;

public class MemberMapper {
    public static SocialMemberCommand toCommand(
            AccountProfileResponse response, AccountType accountType) {
        return new SocialMemberCommand(
                response.id(), response.name(), response.email(), accountType);
    }
}
