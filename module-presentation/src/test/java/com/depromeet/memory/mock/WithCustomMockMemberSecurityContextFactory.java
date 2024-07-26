package com.depromeet.memory.mock;

import com.depromeet.member.Member;
import com.depromeet.memory.fixture.MemberFixture;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockMemberSecurityContextFactory
        implements WithSecurityContextFactory<WithCustomMockMember> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockMember annotation) {
        String userId = annotation.userId();
        String role = annotation.role();

        Member member = MemberFixture.make(Long.parseLong(userId), role);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        member, null, List.of(new SimpleGrantedAuthority(role)));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
