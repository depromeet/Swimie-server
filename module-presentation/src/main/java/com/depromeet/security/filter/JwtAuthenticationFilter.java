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
        log.info("start jwt filter");
        String url = request.getRequestURI();
        if (noAuthentication(url)) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<String> optionalAccessToken =
                Optional.ofNullable(request.getHeader(ACCESS_HEADER.getValue()));

        if (optionalAccessToken.isEmpty()) {
            log.info("access token is empty");
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = optionalAccessToken.get();
        if (!accessToken.startsWith(BEARER_PREFIX.getValue())) {
            log.info("not starts with bearer ");
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7);

        Optional<AccessTokenDto> optionalAccessTokenDto = parseAccessToken(accessToken);

        if (optionalAccessTokenDto.isPresent()) {
            AccessTokenDto accessTokenDto = optionalAccessTokenDto.get();
            setAuthentication(accessTokenDto);
            filterChain.doFilter(request, response);
        } else {
            // 클라이언트에서 refreshToken을 쿠키에 추가할 경우
            /* Optional<String> optionalRefreshToken = Optional.ofNullable(WebUtils.getCookie(request, REFRESH_HEADER.getValue()))
                    .map(Cookie::getValue);
            */
            // 그냥 헤더에 추가해서 보내줄 경우
            Optional<String> optionalRefreshToken =
                    Optional.ofNullable(request.getHeader(REFRESH_HEADER.getValue()));

            if (optionalRefreshToken.isEmpty()) {
                log.info("failed to find refresh token");
                filterChain.doFilter(request, response);
                return;
            }
            String refreshToken = optionalRefreshToken.get();

            Optional<RefreshTokenDto> optionalRefreshTokenDto = parseRefreshToken(refreshToken);

            if (optionalRefreshTokenDto.isEmpty()) {
                log.info("failed to parse refresh token");
                filterChain.doFilter(request, response);
                return;
            }

            RefreshTokenDto refreshTokenDto = optionalRefreshTokenDto.get();

            jwtTokenService.retrieveRefreshToken(refreshTokenDto, refreshToken);
            AccessTokenDto reissuedAccessToken =
                    addReissuedJwtTokenToHeader(response, accessToken, refreshToken);
            setAuthentication(reissuedAccessToken);
            filterChain.doFilter(request, response);
        }
    }

    private boolean noAuthentication(String url) {
        return url.startsWith("/swagger-ui")
                || url.startsWith("/v3")
                || url.startsWith("/favicon.ico")
                || url.startsWith("/oauth2")
                || url.startsWith("/login")
                || url.startsWith("/depromeet-actuator")
                || url.startsWith("/api/v1/auth");
    }

    private Optional<AccessTokenDto> parseAccessToken(String accessToken) {
        Optional<AccessTokenDto> optionalAccessTokenDto;
        try {
            optionalAccessTokenDto = jwtTokenService.parseAccessToken(accessToken);
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
        return optionalAccessTokenDto;
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
            HttpServletResponse response, String accessToken, String refreshToken) {
        AccessTokenDto reissuedAccessToken = jwtTokenService.reissueAccessToken(accessToken);
        RefreshTokenDto reissuedRefreshToken = jwtTokenService.reissueRefreshToken(refreshToken);

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
