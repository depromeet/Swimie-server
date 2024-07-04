package com.depromeet.global.security.filter;

import com.depromeet.domain.auth.service.JwtTokenService;
import com.depromeet.global.security.PrincipalDetails;
import com.depromeet.global.security.jwt.util.AccessTokenDto;
import com.depromeet.global.security.jwt.util.RefreshTokenDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.depromeet.global.security.constant.SecurityConstant.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenService jwtTokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		log.info("start jwt filter");
		Optional<String> optionalAccessToken = Optional.ofNullable(request.getHeader(ACCESS_HEADER.getValue()));

		if (optionalAccessToken.isPresent()
			&& optionalAccessToken.get().startsWith(BEARER_PREFIX.getValue())) {
			String accessToken = optionalAccessToken.get();
			accessToken = accessToken.substring(7);
			Optional<AccessTokenDto> optionalAccessTokenDto = jwtTokenService.parseAccessToken(accessToken);

			if (optionalAccessTokenDto.isPresent()) {
				AccessTokenDto accessTokenDto = optionalAccessTokenDto.get();
				setAuthentication(accessTokenDto);

			} else {
				// 클라이언트에서 refreshToken을 쿠키에 추가할 경우
                /* Optional<String> optionalRefreshToken = Optional.ofNullable(WebUtils.getCookie(request, REFRESH_HEADER.getValue()))
                        .map(Cookie::getValue);
                */
				// 그냥 헤더에 추가해서 보내줄 경우
				Optional<String> optionalRefreshToken = Optional.ofNullable(
					request.getHeader(REFRESH_HEADER.getValue()));
				if (optionalRefreshToken.isPresent()) {
					String refreshToken = optionalRefreshToken.get();
					Optional<RefreshTokenDto> optionalRefreshTokenDto = jwtTokenService.parseRefreshToken(refreshToken);
					if (optionalRefreshTokenDto.isPresent()) {
						RefreshTokenDto refreshTokenDto = optionalRefreshTokenDto.get();

						jwtTokenService.retrieveRefreshToken(refreshTokenDto, refreshToken);
						AccessTokenDto reissuedAccessToken = jwtTokenService.reissueAccessToken(accessToken);
						RefreshTokenDto reissuedRefreshToken = jwtTokenService.reissueRefreshToken(refreshToken);
						log.info("reissued access token: {}", reissuedAccessToken.accessToken());
						log.info("reissued refreshToken token: {}", reissuedRefreshToken.refreshToken());

						response.setHeader(ACCESS_HEADER.getValue(), reissuedAccessToken.accessToken());
						response.setHeader(REFRESH_HEADER.getValue(), reissuedRefreshToken.refreshToken());

						setAuthentication(reissuedAccessToken);

					}
				} else {
					log.info("failed to find refresh token");
					filterChain.doFilter(request, response);
				}

			}

		} else {
			log.info("failed to find access token");
			filterChain.doFilter(request, response);
		}
	}

	private void setAuthentication(AccessTokenDto reissuedAccessToken) {
		UserDetails userDetails = new PrincipalDetails(reissuedAccessToken.memberId(),
			reissuedAccessToken.memberRole());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
			userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
