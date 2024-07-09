package com.depromeet.security.oauth.handler;

import static com.depromeet.security.constant.SecurityConstant.*;
import static com.depromeet.type.member.MemberErrorType.NOT_FOUND;

import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.security.jwt.util.AccessTokenDto;
import com.depromeet.security.jwt.util.JwtUtils;
import com.depromeet.security.jwt.util.RefreshTokenDto;
import com.depromeet.security.oauth.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JwtUtils jwtUtils;
  private final MemberRepository memberRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    log.info("start oauth success handler");

    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    String email = customOAuth2User.getName();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = auth.getAuthority();

    Member member =
        memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    MemberRole memberRole = MemberRole.findByValue(role);

    AccessTokenDto accessTokenDto = jwtUtils.generateAccessToken(member.getId(), memberRole);
    RefreshTokenDto refreshTokenDto = jwtUtils.generateRefreshToken(member.getId());

    addTokenToResponse(response, accessTokenDto, refreshTokenDto);

    response.sendRedirect("http://localhost:3000/");
  }

  private void addTokenToResponse(
      HttpServletResponse response,
      AccessTokenDto accessTokenDto,
      RefreshTokenDto refreshTokenDto) {

    response.addHeader(
        ACCESS_HEADER.getValue(), BEARER_PREFIX.getValue() + accessTokenDto.accessToken());
    response.addHeader(
        REFRESH_HEADER.getValue(), BEARER_PREFIX.getValue() + refreshTokenDto.refreshToken());
  }
}
