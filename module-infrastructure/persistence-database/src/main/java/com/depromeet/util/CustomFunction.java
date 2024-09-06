package com.depromeet.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class CustomFunction {
    public static BooleanExpression match(StringPath target, String keyword) {
        if (keyword == null) {
            return null;
        }

        return Expressions.numberTemplate(
                        java.lang.Double.class,
                        "FUNCTION('MATCH_AGAINST', {0}, {1})",
                        target,
                        keyword)
                .gt(0);
    }
}
