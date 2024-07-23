package com.depromeet.common.dto;

import java.util.List;
import lombok.Builder;
import org.springframework.validation.BindingResult;

public record FieldError(String field, Object rejectedValue, String reason) {
    @Builder
    public FieldError {}

    public static List<FieldError> of(BindingResult bindingResult) {
        List<org.springframework.validation.FieldError> fieldErrors =
                bindingResult.getFieldErrors();

        return fieldErrors.stream()
                .map(
                        error ->
                                FieldError.builder()
                                        .field(String.format("valid_%s", error.getField()))
                                        .rejectedValue(
                                                error.getRejectedValue() != null
                                                        ? error.getRejectedValue().toString()
                                                        : "")
                                        .reason(error.getDefaultMessage())
                                        .build())
                .toList();
    }
}
