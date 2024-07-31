package com.depromeet.memory.annotation;

import com.depromeet.memory.validator.HalfFloatValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HalfFloatValidator.class)
public @interface HalfFloatCheck {
    String message() default "바퀴 수는 0.5 단위로만 입력 가능합니다";

    Class[] groups() default {};

    Class[] payload() default {};
}
