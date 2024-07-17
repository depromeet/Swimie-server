package com.depromeet.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleAccountProfileResponse(
        String id,
        String email,
        String name
) {
}
