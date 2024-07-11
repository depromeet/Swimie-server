package com.depromeet.security.oauth.handler;

import static com.depromeet.security.constant.SecurityConstant.*;
import static com.depromeet.type.member.MemberErrorType.NOT_FOUND;

import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.security.jwt.util.AccessTokenDto;
import com.depromeet.security.jwt.util.RefreshTokenDto;
import com.depromeet.security.oauth.CustomOAuth2User;
import com.depromeet.type.auth.AuthSuccessType;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("start oauth success handler");

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = customOAuth2User.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        Member member =
                memberRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        MemberRole memberRole = MemberRole.findByValue(role);

        JwtTokenResponseDto jwtTokenResponseDto =
                jwtTokenService.generateToken(member.getId(), memberRole);

        // JSON으로 토큰을 응답하는 방식
        response.addHeader("Content-Type", "application/json; charset=UTF-8");

        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(
                                ApiResponse.success(
                                        AuthSuccessType.LOGIN_SUCCESS,
                                        jwtTokenResponseDto))); // json 응답
    }

    // 헤더에 토큰을 담아서 응답하는 방식
    private void addTokenToResponse(
            HttpServletResponse response,
            AccessTokenDto accessTokenDto,
            RefreshTokenDto refreshTokenDto) {

        response.addHeader(
                ACCESS_HEADER.getValue(), BEARER_PREFIX.getValue() + accessTokenDto.accessToken());
        response.addHeader(
                REFRESH_HEADER.getValue(),
                BEARER_PREFIX.getValue() + refreshTokenDto.refreshToken());
    }
}
