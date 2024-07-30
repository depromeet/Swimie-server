package com.depromeet.jwt.util;

public record ErrorResponse(Integer status, String code, String message) {}
