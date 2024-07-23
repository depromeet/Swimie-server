package com.depromeet.common.dto;

import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import lombok.Builder;

public record ConstraintViolationError(String propertyPath, Object rejectedValue, String reason) {
    @Builder
    public ConstraintViolationError {}

    public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(
                        violation ->
                                ConstraintViolationError.builder()
                                        .propertyPath(violation.getPropertyPath().toString())
                                        .rejectedValue(violation.getInvalidValue().toString())
                                        .reason(violation.getMessage())
                                        .build())
                .toList();
    }
}
