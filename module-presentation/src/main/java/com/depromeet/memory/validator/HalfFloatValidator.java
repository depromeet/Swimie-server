package com.depromeet.memory.validator;

import com.depromeet.memory.annotation.HalfFloatCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HalfFloatValidator implements ConstraintValidator<HalfFloatCheck, Float> {
    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return value % 0.5 == 0F;
    }
}
