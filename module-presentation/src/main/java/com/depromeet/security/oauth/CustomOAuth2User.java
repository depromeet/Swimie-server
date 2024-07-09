package com.depromeet.security.oauth;

import com.depromeet.security.oauth.dto.MemberDto;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
  private final MemberDto memberDto;

  public CustomOAuth2User(MemberDto memberDto) {
    this.memberDto = memberDto;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(memberDto.memberRole().getValue()));
  }

  @Override
  public String getName() {
    return memberDto.email();
  }
}
