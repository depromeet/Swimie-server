package com.depromeet.common;

import com.depromeet.common.dto.ConstraintViolationError;
import com.depromeet.common.dto.FieldError;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse {
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> constraintViolations;

    public ValidationErrorResponse(
            List<FieldError> fieldErrors, List<ConstraintViolationError> constraintViolations) {
        this.fieldErrors = fieldErrors;
        this.constraintViolations = constraintViolations;
    }

    public static ValidationErrorResponse of(BindingResult bindingResult) {
        return new ValidationErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ValidationErrorResponse of(Set<ConstraintViolation<?>> constraintViolations) {
        return new ValidationErrorResponse(null, ConstraintViolationError.of(constraintViolations));
    }
}
