package com.depromeet.auth.vo.apple;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppleAccountCommand {
    private String fullName;
    private String email;
    private final String code;
    private final String idToken;

    @Builder
    public AppleAccountCommand(String fullName, String email, String code, String idToken) {
        this.fullName = fullName;
        this.email = email;
        this.code = code;
        this.idToken = idToken;
    }
}
