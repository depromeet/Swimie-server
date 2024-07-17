package com.depromeet.auth.dto.response;

public record AccountProfileResponse(
        String id,
        String name,
        String email
) {
}
