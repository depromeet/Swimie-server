package com.depromeet.security.filter;

import static com.depromeet.security.constant.SecurityConstant.*;
import static com.depromeet.type.auth.AuthErrorType.INVALID_JWT_TOKEN;
import static com.depromeet.type.auth.AuthErrorType.JWT_TOKEN_EXPIRED;

import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.exception.UnauthorizedException;
import com.depromeet.security.jwt.util.AccessTokenDto;
import com.depromeet.security.jwt.util.RefreshTokenDto;
import com.depromeet.security.oauth.CustomOAuth2User;
import com.depromeet.security.oauth.dto.MemberDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url = request.getRequestURI();
        if (noAuthentication(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<String> optionalAccessToken =
                Optional.ofNullable(request.getHeader(ACCESS_HEADER.getValue()));
        Optional<String> optionalRefreshToken =
                Optional.ofNullable(request.getHeader(REFRESH_HEADER.getValue()));

        if (optionalAccessToken.isEmpty() && optionalRefreshToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (optionalAccessToken.isEmpty()) {
            reissueJWTWithRefreshToken(request, response, filterChain);
            return;
        }

        String accessToken = optionalAccessToken.get();
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7);
        Optional<AccessTokenDto> optionalAccessTokenDto = parseAccessToken(accessToken);

        if (optionalAccessTokenDto.isPresent()) {
            AccessTokenDto accessTokenDto = optionalAccessTokenDto.get();
            setAuthentication(accessTokenDto);
        } // 만료된 경우 401 error
        filterChain.doFilter(request, response);
    }

    private boolean noAuthentication(String url) {
        return url.startsWith("/swagger-ui")
                || url.startsWith("/v3")
                || url.startsWith("/favicon.ico")
                || url.startsWith("/oauth2")
                || url.startsWith("/login")
                || url.startsWith("/depromeet-actuator")
                || url.startsWith("/api/v1/auth")
                || url.startsWith("/api/login");
    }

    private Optional<AccessTokenDto> parseAccessToken(String accessToken) {
        Optional<AccessTokenDto> optionalAccessTokenDto;
        try {
            optionalAccessTokenDto = jwtTokenService.parseAccessToken(accessToken);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("JWT expired");
            return Optional.empty();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return optionalAccessTokenDto;
    }

    private void reissueJWTWithRefreshToken(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Optional<String> optionalRefreshToken =
                Optional.ofNullable(request.getHeader(REFRESH_HEADER.getValue()));

        if (optionalRefreshToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = optionalRefreshToken.get();
        if (!refreshToken.startsWith(BEARER_PREFIX.getValue())) {
            filterChain.doFilter(request, response);
            return;
        }
        refreshToken = refreshToken.substring(7);

        Optional<RefreshTokenDto> optionalRefreshTokenDto = parseRefreshToken(refreshToken);
        if (optionalRefreshTokenDto.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        RefreshTokenDto refreshTokenDto = optionalRefreshTokenDto.get();
        jwtTokenService.retrieveRefreshToken(refreshTokenDto, refreshToken);
        AccessTokenDto reissuedAccessToken = addReissuedJwtTokenToHeader(response, refreshToken);
        setAuthentication(reissuedAccessToken);
        filterChain.doFilter(request, response);
    }

    private Optional<RefreshTokenDto> parseRefreshToken(String refreshToken) {
        Optional<RefreshTokenDto> optionalRefreshTokenDto;
        try {
            optionalRefreshTokenDto = jwtTokenService.parseRefreshToken(refreshToken);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(JWT_TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return optionalRefreshTokenDto;
    }

    private AccessTokenDto addReissuedJwtTokenToHeader(
            HttpServletResponse response, String refreshToken) {
        RefreshTokenDto reissuedRefreshToken = jwtTokenService.reissueRefreshToken(refreshToken);
        AccessTokenDto reissuedAccessToken =
                jwtTokenService.reissueAccessToken(reissuedRefreshToken.memberId());

        response.addHeader(ACCESS_HEADER.getValue(), reissuedAccessToken.accessToken());
        response.addHeader(REFRESH_HEADER.getValue(), reissuedRefreshToken.refreshToken());
        return reissuedAccessToken;
    }

    private void setAuthentication(AccessTokenDto reissuedAccessToken) {
        CustomOAuth2User customOAuth2User =
                new CustomOAuth2User(
                        MemberDto.builder()
                                .id(reissuedAccessToken.memberId())
                                .memberRole(reissuedAccessToken.memberRole())
                                .build());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
