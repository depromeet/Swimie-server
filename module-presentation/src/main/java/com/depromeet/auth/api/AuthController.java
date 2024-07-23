package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.facade.AuthFacade;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.type.auth.AuthSuccessType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController implements AuthApi {
    public final AuthFacade authFacade;

    @PostMapping("/google")
    public ApiResponse<JwtTokenResponseDto> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request) {
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByGoogle(request));
    }

    @PostMapping("/kakao")
    public ApiResponse<JwtTokenResponseDto> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request) {
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByKakao(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtAccessTokenResponse> reissueAccessToken(
            @RequestHeader("Authorization") String refreshToken) {
        return ApiResponse.success(
                AuthSuccessType.REISSUE_ACCESS_TOKEN_SUCCESS,
                authFacade.getReissuedAccessToken(refreshToken));
    }
}
