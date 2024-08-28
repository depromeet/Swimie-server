package com.depromeet.member.port.in.command;

public record SocialMemberCommand(String id, String name, String email, String providerId, String defaultProfile) {}
