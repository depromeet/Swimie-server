package com.depromeet.auth.port.out;

import com.depromeet.dto.auth.AccountProfileResponse;

public interface ApplePort {
    AccountProfileResponse getAppleAccountToken(String code, String origin);

    void revokeAccount(String providerId);
}
