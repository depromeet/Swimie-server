package com.depromeet.config.mock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockMemberSecurityContextFactory.class)
public @interface WithCustomMockMember {
    String userId() default "1";

    String role() default "USER";
}
