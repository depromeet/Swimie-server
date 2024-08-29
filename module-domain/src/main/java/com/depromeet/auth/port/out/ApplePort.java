package com.depromeet.auth.port.out;

import com.depromeet.auth.vo.apple.AppleAccountCommand;
import com.depromeet.dto.auth.AccountProfileResponse;

public interface ApplePort {
    AccountProfileResponse getAppleAccountToken(
            AppleAccountCommand appleAccountCommand, String origin);

    void revokeAccount(String providerId);
}
