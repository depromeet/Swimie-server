package com.depromeet.dto.response;

import com.depromeet.exception.BaseException;
import com.depromeet.type.ErrorType;
import com.depromeet.type.SuccessType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "code", "message", "data"})
public record ApiResponse<T>(
        int status,
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) T data) {
    public static ApiResponse<?> success(SuccessType successType) {
        return new ApiResponse<>(200, successType.getCode(), successType.getMessage(), null);
    }

    public static <T> ApiResponse<T> success(SuccessType successType, T data) {
        return new ApiResponse<>(200, successType.getCode(), successType.getMessage(), data);
    }

    public static ApiResponse<?> fail(BaseException exception) {
        ErrorType errorType = exception.getErrorType();
        return new ApiResponse<>(
                exception.getHttpCode(), errorType.getCode(), errorType.getMessage(), null);
    }

    public static <T> ApiResponse<T> fail(BaseException exception, T data) {
        ErrorType errorType = exception.getErrorType();
        return new ApiResponse<>(
                exception.getHttpCode(), errorType.getCode(), errorType.getMessage(), data);
    }

    public static ApiResponse<?> fail(ErrorType errorType, int httpCode) {
        return new ApiResponse<>(httpCode, errorType.getCode(), errorType.getMessage(), null);
    }

    public static <T> ApiResponse<?> fail(ErrorType errorType, int httpCode, T data) {
        return new ApiResponse<>(httpCode, errorType.getCode(), errorType.getMessage(), data);
    }
}
