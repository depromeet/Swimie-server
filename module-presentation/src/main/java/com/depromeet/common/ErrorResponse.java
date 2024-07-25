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
public class ErrorResponse {
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> constraintViolations;

    public ErrorResponse(
            List<FieldError> fieldErrors, List<ConstraintViolationError> constraintViolations) {
        this.fieldErrors = fieldErrors;
        this.constraintViolations = constraintViolations;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> constraintViolations) {
        return new ErrorResponse(null, ConstraintViolationError.of(constraintViolations));
    }
}
