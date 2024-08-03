package com.depromeet.member.port.in.command;

import com.depromeet.auth.domain.AccountType;

public record SocialMemberCommand(String id, String name, String email, AccountType accountType) {}
