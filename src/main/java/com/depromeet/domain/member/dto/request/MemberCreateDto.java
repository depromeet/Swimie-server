package com.depromeet.domain.member.dto.request;

import java.util.Objects;

public record MemberCreateDto(String name, String email, String password) {

    public MemberCreateDto {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
