package com.depromeet.member.dto.request;

import com.depromeet.annotation.Enum;
import com.depromeet.member.domain.MemberGender;
import jakarta.validation.constraints.NotBlank;

public record GenderUpdateRequest(
        @NotBlank @Enum(enumClass = MemberGender.class, ignoreCase = true) String gender) {}
