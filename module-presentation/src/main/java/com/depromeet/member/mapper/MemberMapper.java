package com.depromeet.member.mapper;

import com.depromeet.dto.auth.AccountProfileResponse;
import com.depromeet.member.dto.request.MemberUpdateRequest;
import com.depromeet.member.port.in.command.SocialMemberCommand;
import com.depromeet.member.port.in.command.UpdateMemberCommand;

public class MemberMapper {
    public static SocialMemberCommand toCommand(
            AccountProfileResponse response, String providerId, String defaultProfile) {
        return new SocialMemberCommand(
                response.id(), response.name(), response.email(), providerId, defaultProfile);
    }

    public static UpdateMemberCommand toCommand(
            Long memberId, MemberUpdateRequest memberUpdateRequest) {
        return new UpdateMemberCommand(
                memberId, memberUpdateRequest.nickname(), memberUpdateRequest.introduction());
    }
}
