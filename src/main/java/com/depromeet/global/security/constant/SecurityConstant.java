package com.depromeet.global.security.constant;

import lombok.Getter;

@Getter
public enum SecurityConstant {
    ACCESS_HEADER("Authorization"),
    REFRESH_HEADER("refresh"),
    BEARER_PREFIX("Bearer ");

    private String value;

    SecurityConstant(String value) {
        this.value = value;
    }
}
