package com.depromeet.security.filter;

import static com.depromeet.security.constant.SecurityConstant.*;

import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.exception.BadRequestException;
import com.depromeet.exception.NotFoundException;
import com.depromeet.member.MemberRole;
import com.depromeet.security.jwt.util.AccessTokenDto;
import com.depromeet.security.jwt.util.RefreshTokenDto;
import com.depromeet.security.oauth.CustomOAuth2User;
import com.depromeet.security.oauth.dto.MemberDto;
import com.depromeet.type.auth.AuthErrorType;
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
        String token = request.getHeader(AUTH_HEADER.getValue());

        if (token.isEmpty()) {
            log.info("token is empty");
            throw new NotFoundException(AuthErrorType.JWT_TOKEN_NOT_FOUND);
        }

        if (!token.startsWith(BEARER_PREFIX.getValue())) {
            log.info("not starts with Bearer");
            throw new NotFoundException(AuthErrorType.JWT_TOKEN_PREFIX);
        }

        token = token.substring(7);
        String tokenType = jwtTokenService.findTokenType(token);

        if (tokenType.equals(ACCESS.getValue())) {
            if (url.equals("/api/login/refresh")) {
                throw new BadRequestException(AuthErrorType.INVALID_JWT_ACCESS_REQUEST);
            }

            Optional<AccessTokenDto> optionalAccessTokenDto =
                    jwtTokenService.parseAccessToken(token);

            if (optionalAccessTokenDto.isPresent()) {
                AccessTokenDto accessTokenDto = optionalAccessTokenDto.get();
                setAuthentication(accessTokenDto.memberId(), accessTokenDto.memberRole());
            }
        } else if (tokenType.equals(REFRESH.getValue())) {
            if (!url.equals("/api/login/refresh")) {
                throw new BadRequestException(AuthErrorType.INVALID_JWT_REFRESH_REQUEST);
            }
            Optional<RefreshTokenDto> optionalRefreshTokenDto =
                    jwtTokenService.parseRefreshToken(token);

            if (optionalRefreshTokenDto.isEmpty()) {
                log.info("failed to parse refresh token");
                throw new NotFoundException(AuthErrorType.JWT_REFRESH_TOKEN_NOT_FOUND);
            }

            RefreshTokenDto refreshTokenDto = optionalRefreshTokenDto.get();

            setAuthentication(refreshTokenDto.memberId(), refreshTokenDto.memberRole());
        }
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
                || url.equals("/api/login/kakao")
                || url.equals("/api/login/google");
    }

    private AccessTokenDto addReissuedJwtTokenToHeader(
            HttpServletResponse response, String refreshToken) {
        AccessTokenDto reissuedAccessToken = jwtTokenService.reissueAccessToken(refreshToken);

        response.addHeader(AUTH_HEADER.getValue(), reissuedAccessToken.accessToken());
        return reissuedAccessToken;
    }

    private void setAuthentication(Long memberId, MemberRole memberRole) {
        CustomOAuth2User customOAuth2User =
                new CustomOAuth2User(
                        MemberDto.builder().id(memberId).memberRole(memberRole).build());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
