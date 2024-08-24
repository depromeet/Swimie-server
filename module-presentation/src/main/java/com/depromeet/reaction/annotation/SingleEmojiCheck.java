package com.depromeet.reaction.annotation;

import com.depromeet.reaction.validator.SingleEmojiValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SingleEmojiValidator.class)
public @interface SingleEmojiCheck {
    String message() default "이모지 한 개를 입력해야 합니다";

    Class[] groups() default {};

    Class[] payload() default {};
}
