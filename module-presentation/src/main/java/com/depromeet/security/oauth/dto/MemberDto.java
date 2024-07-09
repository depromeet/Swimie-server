package com.depromeet.security.oauth.dto;

import com.depromeet.member.MemberRole;

public record MemberDto(String email, MemberRole memberRole) {}
