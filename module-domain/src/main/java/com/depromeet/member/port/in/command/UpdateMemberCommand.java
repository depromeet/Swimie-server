package com.depromeet.member.port.in.command;

public record UpdateMemberCommand(Long memberId, String nickname, String introduction) {}
