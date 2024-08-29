package com.depromeet.auth.mapper;

import com.depromeet.auth.dto.request.AppleLoginRequest;
import com.depromeet.auth.vo.apple.AppleAccountCommand;

public class AppleMapper {
    public static AppleAccountCommand toCommand(AppleLoginRequest request) {
        if (request.user() != null) {
            return AppleAccountCommand.builder()
                    .fullName(request.user().name().firstName() + request.user().name().lastName())
                    .email(request.user().email())
                    .code(request.code())
                    .idToken(request.idToken())
                    .build();
        } else {
            return AppleAccountCommand.builder()
                    .code(request.code())
                    .idToken(request.idToken())
                    .build();
        }
    }
}
