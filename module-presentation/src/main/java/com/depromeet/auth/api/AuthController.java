package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.service.AuthService;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.type.auth.AuthSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController implements AuthApi {
    private final AuthService authService;

    @PostMapping("/google")
    public ApiResponse<JwtTokenResponseDto> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request) {
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authService.loginByGoogle(request));
    }

    @PostMapping("/kakao")
    public ApiResponse<JwtTokenResponseDto> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request) {
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authService.loginByKakao(request));
    }
}
