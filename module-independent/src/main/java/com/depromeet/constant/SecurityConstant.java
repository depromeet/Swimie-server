package com.depromeet.constant;

import lombok.Getter;

@Getter
public enum SecurityConstant {
    AUTH_HEADER("Authorization"),
    BEARER_PREFIX("Bearer "),
    ACCESS("access"),
    REFRESH("refresh");

    private String value;

    SecurityConstant(String value) {
        this.value = value;
    }
}
