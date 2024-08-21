package com.depromeet.reaction.validator;

import com.depromeet.reaction.annotation.SingleEmojiCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.BreakIterator;

public class SingleEmojiValidator implements ConstraintValidator<SingleEmojiCheck, String> {
    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(text);
        int count = 0;
        while (it.current() < text.length()) {
            if (Character.isEmoji(text.codePointAt(it.current()))) {
                ++count;
            } else {
                return false;
            }
            it.next();
        }

        return count == 1;
    }
}
