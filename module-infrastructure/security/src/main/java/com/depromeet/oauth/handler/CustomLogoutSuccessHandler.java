package com.depromeet.oauth.handler;

import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.type.auth.AuthSuccessType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final JwtTokenService jwtTokenService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        // 로그아웃에 성공하면 redis refresh token 삭제
        String header = request.getHeader("Authorization");
        jwtTokenService.destroyRefreshToken(header);
        response.addHeader("Content-Type", "application/json; charset=UTF-8");
        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(
                                ApiResponse.success(AuthSuccessType.LOGOUT_SUCCESS))); // json 응답
    }
}
