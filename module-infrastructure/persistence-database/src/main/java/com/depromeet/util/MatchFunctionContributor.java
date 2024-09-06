package com.depromeet.util;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class MatchFunctionContributor implements FunctionContributor {
    private static final String FUNCTION_NAME = "MATCH_AGAINST";
    private static final String FUNCTION_PATTERN = "MATCH (?1) AGAINST (?2 IN BOOLEAN MODE)";

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions
                .getFunctionRegistry()
                .registerPattern(
                        FUNCTION_NAME,
                        FUNCTION_PATTERN,
                        functionContributions
                                .getTypeConfiguration()
                                .getBasicTypeRegistry()
                                .resolve(StandardBasicTypes.DOUBLE));
    }
}
