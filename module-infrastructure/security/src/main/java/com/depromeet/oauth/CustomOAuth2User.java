package com.depromeet.oauth;

import com.depromeet.member.domain.MemberRole;
import com.depromeet.oauth.dto.MemberDto;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User, UserDetails {
    private Long id;
    private String name;
    private String email;
    private MemberRole memberRole;

    public CustomOAuth2User(MemberDto memberDto) {
        this.id = memberDto.getId();
        this.name = memberDto.getName();
        this.email = memberDto.getEmail();
        this.memberRole = memberDto.getMemberRole();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.memberRole.getValue()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Long getId() {
        return this.id;
    }

    public MemberRole getMemberRole() {
        return this.memberRole;
    }
}
