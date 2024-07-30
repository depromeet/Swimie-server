package com.depromeet.auth.port.out;

import com.depromeet.dto.auth.AccountProfileResponse;

public interface GooglePort {
    AccountProfileResponse getGoogleAccountProfile(String code);
}
